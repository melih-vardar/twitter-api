package com.example.twitter.service;

import com.example.twitter.dto.tweet.CreateTweetDto;
import com.example.twitter.dto.tweet.TweetListingDto;
import com.example.twitter.dto.tweet.UpdateTweetDto;

import java.util.List;
import java.util.UUID;

public interface TweetService {
    CreateTweetDto create(CreateTweetDto createTweetDto);
    List<TweetListingDto> findByUserId(UUID userId);
    TweetListingDto findById(Integer id);
    CreateTweetDto update(Integer id, UpdateTweetDto updateTweetDto);
    void delete(Integer id);
    List<TweetListingDto> findCurrentUserTweets();
} 