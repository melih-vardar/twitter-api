package com.example.twitter.controller;

import com.example.twitter.dto.retweet.CreateRetweetDto;
import com.example.twitter.dto.retweet.RetweetListingDto;
import com.example.twitter.service.RetweetService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/retweet")
@AllArgsConstructor
public class RetweetController {
    private final RetweetService retweetService;

    @PostMapping
    public RetweetListingDto create(@Valid @RequestBody CreateRetweetDto createRetweetDto) {
        return retweetService.create(createRetweetDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        retweetService.delete(id);
    }
} 