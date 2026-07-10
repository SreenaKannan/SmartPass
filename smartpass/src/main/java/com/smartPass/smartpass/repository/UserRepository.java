package com.smartPass.smartpass.repository;

import com.smartPass.smartpass.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository
        extends JpaRepository<User, Long> {

    User findByEmail(String email);
}