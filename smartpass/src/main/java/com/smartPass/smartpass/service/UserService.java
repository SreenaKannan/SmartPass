package com.smartPass.smartpass.service;

import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.smartPass.smartpass.dto.LoginDTO;
import com.smartPass.smartpass.dto.LoginResponseDTO;
import com.smartPass.smartpass.security.TokenService;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    public UserService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public String registerUser(User user) {

        User existing = userRepository.findByEmail(user.getEmail());
        // Repairs legacy records created before passwords were writable in JSON.
        if (existing != null) {
            if (existing.getPassword() == null && user.getPassword() != null) {
                existing.setPassword(user.getPassword());
                if (existing.getRole() == null) existing.setRole("USER");
                userRepository.save(existing);
                return "Legacy account password restored. Please log in.";
            }
            return "Email is already registered";
        }
        user.setRole("USER");
        userRepository.save(user);

        return "User " + user.getName() + " registered successfully!";
    }


public String loginUser(String email, String password) {

    User user = userRepository.findByEmail(email);

    if (user != null && user.getPassword() != null && user.getPassword().equals(password)) {

        return "Login Successful";

    }

    return "Invalid Email or Password";
}

public LoginResponseDTO login(LoginDTO loginDTO) {

    User user =
            userRepository.findByEmail(
                    loginDTO.getEmail());

    if (user == null) {
        throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    if (user.getPassword() == null || !user.getPassword().equals(
            loginDTO.getPassword())) {

        throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    return new LoginResponseDTO(tokenService.issue(user), user.getId(), user.getName(), user.getRole());
}

}
