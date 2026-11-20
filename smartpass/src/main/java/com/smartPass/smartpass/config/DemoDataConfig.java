
package com.smartPass.smartpass.config;

import com.smartPass.smartpass.entity.User;
import com.smartPass.smartpass.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoDataConfig {

    @Bean
    CommandLineRunner demoUsers(UserRepository users) {
        return args -> {
            create(users, "SmartPass Administrator",
                    "admin@smartpass.com", "9000000001",
                    "Admin@123", "ADMIN");

            create(users, "Demo Commuter",
                    "user@smartpass.com", "9000000002",
                    "User@123", "USER");
        };
    }

    private void create(UserRepository users, String name,
                        String email, String phone,
                        String password, String role) {

        if (users.findByEmail(email) == null) {
            User u = new User();
            u.setName(name);
            u.setEmail(email);
            u.setPhone(phone);
            u.setPassword(password);
            u.setRole(role);

            users.save(u);
        }
    }
}
