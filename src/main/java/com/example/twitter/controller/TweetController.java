package com.example.twitter.controller;

import com.example.twitter.dto.tweet.CreateTweetDto;
import com.example.twitter.dto.tweet.TweetListingDto;
import com.example.twitter.dto.tweet.UpdateTweetDto;
import com.example.twitter.service.TweetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tweet")
@AllArgsConstructor
public class TweetController {
    private final TweetService tweetService;

    @PostMapping
    public CreateTweetDto create(@Valid @RequestBody CreateTweetDto createTweetDto) {
        return tweetService.create(createTweetDto);
    }

    @GetMapping("/findByUserId")
    public List<TweetListingDto> findByUserId(@RequestParam UUID userId) {
        return tweetService.findByUserId(userId);
    }

    @GetMapping("/findById")
    public TweetListingDto findById(@RequestParam Integer id) {
        return tweetService.findById(id);
    }

    @PutMapping("/{id}")
    public CreateTweetDto update(@PathVariable Integer id, @Valid @RequestBody UpdateTweetDto updateTweetDto) {
        return tweetService.update(id, updateTweetDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        tweetService.delete(id);
    }
} 