package com.example.twitter.controller;

import com.example.twitter.dto.retweet.CreateRetweetDto;
import com.example.twitter.dto.retweet.RetweetListingDto;
import com.example.twitter.service.RetweetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/retweet")
@AllArgsConstructor
public class RetweetController {
    private final RetweetService retweetService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RetweetListingDto create(@Valid @RequestBody CreateRetweetDto createRetweetDto) {
        return retweetService.create(createRetweetDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        retweetService.delete(id);
    }

    @GetMapping("/findByUserId")
    @ResponseStatus(HttpStatus.OK)
    public List<RetweetListingDto> getRetweetsByUserId(@RequestParam UUID userId) {
        return retweetService.getRetweetsByUserId(userId);
    }

    @GetMapping("/my-retweets")
    @ResponseStatus(HttpStatus.OK)
    public List<RetweetListingDto> getCurrentUserRetweets() {
        return retweetService.getCurrentUserRetweets();
    }
} 