package com.example.twitter.rules;

import com.example.twitter.entity.Tweet;
import com.example.twitter.repository.TweetRepository;
import com.example.twitter.service.UserService;
import com.example.twitter.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class TweetBusinessRules {
    private final TweetRepository tweetRepository;
    private final UserService userService;

    public void checkIfTweetExists(Integer id) {
        if (!tweetRepository.existsById(id)) {
            throw new BusinessException("Tweet not found");
        }
    }

    public void checkIfUserIsOwner(Integer tweetId) {
        Tweet tweet = tweetRepository.findById(tweetId)
            .orElseThrow(() -> new BusinessException("Tweet not found"));

        UUID userId = userService.getActiveUserId();
        if (!tweet.getUser().getId().equals(userId)) {
            throw new BusinessException("You are not authorized to perform this action");
        }
    }
} 