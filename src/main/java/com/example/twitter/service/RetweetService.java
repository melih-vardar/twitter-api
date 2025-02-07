package com.example.twitter.service;

import com.example.twitter.dto.retweet.CreateRetweetDto;
import com.example.twitter.dto.retweet.RetweetListingDto;

public interface RetweetService {
    RetweetListingDto create(CreateRetweetDto createRetweetDto);
    void delete(Integer id);
} 