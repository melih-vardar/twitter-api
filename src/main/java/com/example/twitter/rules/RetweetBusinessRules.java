package com.example.twitter.rules;

import com.example.twitter.entity.Retweet;
import com.example.twitter.repository.RetweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RetweetBusinessRules {
    private final RetweetRepository retweetRepository;

    public void checkIfRetweetExists(Integer id) {
        if (!retweetRepository.existsById(id)) {
            throw new RuntimeException("Retweet not found");
        }
    }

    public void checkIfUserIsOwner(Integer retweetId, UUID userId) {
        Retweet retweet = retweetRepository.findById(retweetId)
            .orElseThrow(() -> new RuntimeException("Retweet not found"));
        
        if (!retweet.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to perform this action");
        }
    }
} 