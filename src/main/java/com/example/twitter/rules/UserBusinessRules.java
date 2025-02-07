package com.example.twitter.rules;


import com.example.twitter.repository.UserRepository;
import com.example.twitter.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserBusinessRules {
    private final UserRepository userRepository;

    public void usernameMustNotExist(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            throw new BusinessException("Username already exist");
        });
    }
}