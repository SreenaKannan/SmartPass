package com.smartPass.smartpass.service;

import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.smartPass.smartpass.dto.LoginDTO;
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String registerUser(User user) {

        userRepository.save(user);

        return "User " + user.getName() + " registered successfully!";
    }


public String loginUser(String email, String password) {

    User user = userRepository.findByEmail(email);

    if (user != null && user.getPassword().equals(password)) {

        return "Login Successful";

    }

    return "Invalid Email or Password";
}

public String login(LoginDTO loginDTO) {

    User user =
            userRepository.findByEmail(
                    loginDTO.getEmail());

    if (user == null) {
        return "Invalid Email";
    }

    if (!user.getPassword().equals(
            loginDTO.getPassword())) {

        return "Invalid Password";
    }

    return "Login Successful";
}

}