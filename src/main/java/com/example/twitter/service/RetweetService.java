package com.example.twitter.service;

import com.example.twitter.dto.retweet.CreateRetweetDto;
import com.example.twitter.dto.retweet.RetweetListingDto;
import java.util.List;
import java.util.UUID;

public interface RetweetService {
    RetweetListingDto create(CreateRetweetDto createRetweetDto);
    void delete(Integer id);
    List<RetweetListingDto> getRetweetsByUserId(UUID userId);
    List<RetweetListingDto> getCurrentUserRetweets();
} 