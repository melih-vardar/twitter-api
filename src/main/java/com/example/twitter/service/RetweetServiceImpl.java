package com.example.twitter.service;

import com.example.twitter.rules.RetweetBusinessRules;
import com.example.twitter.dto.retweet.CreateRetweetDto;
import com.example.twitter.dto.retweet.RetweetListingDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.entity.Retweet;
import com.example.twitter.repository.RetweetRepository;
import com.example.twitter.repository.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RetweetServiceImpl implements RetweetService {
    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final RetweetBusinessRules retweetBusinessRules;
    private final UserService userService;

    @Override
    public RetweetListingDto create(CreateRetweetDto createRetweetDto) {
        Retweet retweet = new Retweet();
        retweet.setUser(userService.getActiveUser());

        retweet.setOriginalTweet(tweetRepository.findById(createRetweetDto.getOriginalTweetId())
                .orElseThrow(() -> new RuntimeException("Tweet not found")));

        retweet.setQuote(createRetweetDto.getQuote());
        retweet.setCreatedAt(LocalDateTime.now());
        retweetRepository.save(retweet);
        return convertToListingDto(retweet);
    }

    @Override
    public void delete(Integer id) {
        retweetBusinessRules.checkIfRetweetExists(id);
        retweetBusinessRules.checkIfUserIsOwner(id, userService.getActiveUser().getId());
        retweetRepository.deleteById(id);
    }

    private RetweetListingDto convertToListingDto(Retweet retweet) {
        RetweetListingDto dto = new RetweetListingDto();
        dto.setId(retweet.getId());
        dto.setOriginalTweetId(retweet.getOriginalTweet().getId());
        
        UserListingDto userDto = new UserListingDto();
        userDto.setId(retweet.getUser().getId());
        userDto.setUsername(retweet.getUser().getUsername());
        userDto.setName(retweet.getUser().getName());
        userDto.setSurname(retweet.getUser().getSurname());

        dto.setUser(userDto);
        
        dto.setQuote(retweet.getQuote());
        return dto;
    }
} 