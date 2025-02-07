package com.example.twitter.rules;

import com.example.twitter.entity.Retweet;
import com.example.twitter.repository.RetweetRepository;
import com.example.twitter.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class RetweetBusinessRules {
    private final RetweetRepository retweetRepository;

    public void checkIfRetweetExists(Integer id) {
        if (!retweetRepository.existsById(id)) {
            throw new BusinessException("Retweet not found");
        }
    }

    public void checkIfUserIsOwner(Integer retweetId, UUID userId) {
        Retweet retweet = retweetRepository.findById(retweetId)
            .orElseThrow(() -> new BusinessException("Retweet not found"));
        
        if (!retweet.getUser().getId().equals(userId)) {
            throw new BusinessException("You are not authorized to perform this action");
        }
    }
} 