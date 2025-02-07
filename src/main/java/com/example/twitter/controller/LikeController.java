package com.example.twitter.controller;

import com.example.twitter.dto.like.CreateLikeDto;
import com.example.twitter.dto.like.LikeListingDto;
import com.example.twitter.service.LikeService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/like")
    public void likeOrDislike(@Valid @RequestBody CreateLikeDto createLikeDto) {
         likeService.likeOrDislike(createLikeDto);
    }

} 