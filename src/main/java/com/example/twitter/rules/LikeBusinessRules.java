package com.example.twitter.rules;

import com.example.twitter.repository.LikeRepository;
import com.example.twitter.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class LikeBusinessRules {
    private final LikeRepository likeRepository;

    public void checkIfAlreadyLiked(Integer tweetId, UUID userId) {
        if (likeRepository.findByUserIdAndTweetId(userId, tweetId).isPresent()) {
            throw new BusinessException("Tweet already liked");
        }
    }

    public void checkIfLikeExists(Integer tweetId, UUID userId) {
        if (likeRepository.findByUserIdAndTweetId(userId, tweetId).isEmpty()) {
            throw new BusinessException("Like not found");
        }
    }
} 