package com.example.twitter.service;

import com.example.twitter.rules.RetweetBusinessRules;
import com.example.twitter.dto.retweet.CreateRetweetDto;
import com.example.twitter.dto.retweet.RetweetListingDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.entity.Retweet;
import com.example.twitter.repository.RetweetRepository;
import com.example.twitter.repository.TweetRepository;
import com.example.twitter.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RetweetServiceImpl implements RetweetService {
    private final RetweetRepository retweetRepository;
    private final TweetRepository tweetRepository;
    private final RetweetBusinessRules retweetBusinessRules;
    private final UserService userService;

    @Override
    public RetweetListingDto create(CreateRetweetDto createRetweetDto) {
        Retweet retweet;
        if(isTweetIsAlreadyRetweeted(createRetweetDto.getOriginalTweetId(), userService.getActiveUserId())){
            retweet = retweetRepository.findByOriginalTweetIdAndUserId(createRetweetDto.getOriginalTweetId(), userService.getActiveUserId()).get();
            delete(retweet.getId());
        }
        else {
            retweet = new Retweet();
            retweet.setUser(userService.getActiveUser());

            retweet.setOriginalTweet(tweetRepository.findById(createRetweetDto.getOriginalTweetId())
                    .orElseThrow(() -> new BusinessException("Tweet not found")));

            retweet.setQuote(createRetweetDto.getQuote());
            retweet.setCreatedAt(LocalDateTime.now());
            retweetRepository.save(retweet);
        }
        return convertToListingDto(retweet);
    }

    @Override
    public void delete(Integer id) {
        retweetBusinessRules.checkIfRetweetExists(id);
        retweetBusinessRules.checkIfUserIsOwner(id, userService.getActiveUser().getId());
        retweetRepository.deleteById(id);
    }

    @Override
    public List<RetweetListingDto> getRetweetsByUserId(UUID userId) {
        return retweetRepository.findByUserId(userId)
                .stream()
                .map(this::convertToListingDto)
                .toList();
    }

    @Override
    public List<RetweetListingDto> getCurrentUserRetweets() {
        return retweetRepository.findByUserId(userService.getActiveUserId())
                .stream()
                .map(this::convertToListingDto)
                .toList();
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
        dto.setStartDate(retweet.getCreatedAt());
        return dto;
    }

    private Boolean isTweetIsAlreadyRetweeted(Integer originalTweetId, UUID userId) {
        return retweetRepository.findByOriginalTweetIdAndUserId(originalTweetId, userId).isPresent();
    }
} 