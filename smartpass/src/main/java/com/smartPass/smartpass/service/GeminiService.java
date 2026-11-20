package com.smartPass.smartpass.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Thin wrapper around the Google Gemini API (generateContent).
 *
 * This is a REAL LLM call - no hardcoded if/else logic. Callers pass a
 * system prompt (the task + any real facts pulled from the database to
 * ground the model) and get back the model's raw JSON text, which the
 * caller then parses into its own DTO.
 */
@Service
public class GeminiService {

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.model}")
    private String model;

    @Value("${gemini.api.url}")
    private String apiBaseUrl;

    public GeminiService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a generateContent request and returns the model's response text
     * as a JSON string. responseMimeType is set to application/json, so the
     * model is constrained to return valid JSON matching what the prompt asks for.
     */
    public String chatJson(String systemPrompt, String userPrompt) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "AI Hub is not configured: missing GEMINI_API_KEY.");
        }

        try {
            var requestBody = objectMapper.createObjectNode();

            var systemInstruction = objectMapper.createObjectNode();
            var systemParts = objectMapper.createArrayNode();
            var systemPart = objectMapper.createObjectNode();
            systemPart.put("text", systemPrompt);
            systemParts.add(systemPart);
            systemInstruction.set("parts", systemParts);
            requestBody.set("systemInstruction", systemInstruction);

            var contents = objectMapper.createArrayNode();
            var userContent = objectMapper.createObjectNode();
            userContent.put("role", "user");
            var userParts = objectMapper.createArrayNode();
            var userPart = objectMapper.createObjectNode();
            userPart.put("text", userPrompt);
            userParts.add(userPart);
            userContent.set("parts", userParts);
            contents.add(userContent);
            requestBody.set("contents", contents);

            var generationConfig = objectMapper.createObjectNode();
            generationConfig.put("temperature", 0.4);
            generationConfig.put("responseMimeType", "application/json");
            requestBody.set("generationConfig", generationConfig);

            String url = apiBaseUrl + "/" + model + ":generateContent";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(30))
                    .header("Content-Type", "application/json")
                    .header("x-goog-api-key", apiKey)
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(requestBody)))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY,
                        "AI service call failed (status " + response.statusCode() + "): " + response.body());
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");
            if (textNode.isMissingNode() || textNode.isNull()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI service returned an unexpected response.");
            }
            return textNode.asText();

        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "AI service call failed: " + e.getMessage(), e);
        }
    }
}
