
package com.smartPass.smartpass.config;

import com.smartPass.smartpass.security.TokenAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenAuthenticationFilter filter;

    public SecurityConfig(TokenAuthenticationFilter filter) {
        this.filter = filter;
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http) throws Exception {

        return http
                .csrf(c -> c.disable())
                .cors(c -> {})
                .sessionManagement(s ->
                        s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(a -> a

                        // Public endpoints
                        .requestMatchers(
                                "/",
                                "/api/users/register",
                                "/api/users/login",
                                "/error"
                        ).permitAll()

                        // Admin-only endpoints
                        .requestMatchers(
                                "/api/admin/**",
                                "/api/cards/approve/**",
                                "/api/cards/reject/**",
                                "/api/cards/block/**",
                                "/api/cards/expired",
                                "/api/cards/search/**",
                                "/api/cards/expiring-soon"
                        ).hasRole("ADMIN")

                        // User-only endpoints
                        .requestMatchers("/api/ai-hub/**").hasRole("USER")

                        // All remaining endpoints require authentication
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        filter,
                        UsernamePasswordAuthenticationFilter.class
                )
                .build();
    }
}
