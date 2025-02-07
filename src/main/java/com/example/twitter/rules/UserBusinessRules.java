package com.example.twitter.rules;


import com.example.twitter.repository.UserRepository;
import com.example.twitter.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UserBusinessRules {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public void usernameMustNotExist(String username) {
        if(userRepository.findByUsername(username) != null) {
            throw new BusinessException("Username already exists.");
        }
    }

    public void userMustBeAuthenticated(Authentication authentication){
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException("You should login or register.");
        }
    }

    public void usernameMustExist(String username){
        if(userRepository.findByUsername(username) == null) {
            throw new BusinessException("Invalid or wrong credentials.");
        }
    }

    public void isPasswordCorrect(String loginPassword, String dbPassword){
        if(!passwordEncoder.matches(loginPassword, dbPassword)) {
            throw new BusinessException("Invalid or wrong credentials.");
        }
    }
}