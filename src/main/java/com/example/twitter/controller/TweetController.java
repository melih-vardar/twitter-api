package com.example.twitter.controller;

import com.example.twitter.dto.tweet.CreateTweetDto;
import com.example.twitter.dto.tweet.TweetListingDto;
import com.example.twitter.dto.tweet.UpdateTweetDto;
import com.example.twitter.service.TweetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/tweet")
@AllArgsConstructor
public class TweetController {
    private final TweetService tweetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateTweetDto create(@Valid @RequestBody CreateTweetDto createTweetDto) {
        return tweetService.create(createTweetDto);
    }

    @GetMapping("/findByUserId")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetListingDto> findByUserId(@RequestParam UUID userId) {
        return tweetService.findByUserId(userId);
    }

    @GetMapping("/findById")
    @ResponseStatus(HttpStatus.OK)
    public TweetListingDto findById(@RequestParam Integer id) {
        return tweetService.findById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public CreateTweetDto update(@PathVariable Integer id, @Valid @RequestBody UpdateTweetDto updateTweetDto) {
        return tweetService.update(id, updateTweetDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        tweetService.delete(id);
    }

    @GetMapping("/my-tweets")
    @ResponseStatus(HttpStatus.OK)
    public List<TweetListingDto> getCurrentUserTweets() {
        return tweetService.findCurrentUserTweets();
    }
} 