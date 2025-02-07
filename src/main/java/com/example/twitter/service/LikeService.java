package com.example.twitter.service;

import com.example.twitter.dto.like.CreateLikeDto;
import com.example.twitter.dto.like.LikeListingDto;

public interface LikeService {
    void likeOrDislike(CreateLikeDto createLikeDto);
    LikeListingDto like(CreateLikeDto createLikeDto);
    void dislike(CreateLikeDto createLikeDto);
} 