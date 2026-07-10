package com.smartPass.smartpass.controller;

import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.smartPass.smartpass.dto.LoginDTO;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        return userService.registerUser(user);

    }

@PostMapping("/login")
public String login(
        @RequestBody LoginDTO loginDTO) {

    return userService.login(loginDTO);
}

}