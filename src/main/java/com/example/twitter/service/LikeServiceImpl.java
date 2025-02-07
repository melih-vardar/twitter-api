package com.example.twitter.service;

import com.example.twitter.rules.LikeBusinessRules;
import com.example.twitter.dto.like.CreateLikeDto;
import com.example.twitter.dto.like.LikeListingDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.entity.Like;
import com.example.twitter.repository.LikeRepository;
import com.example.twitter.repository.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final TweetRepository tweetRepository;
    private final LikeBusinessRules rules;
    private final UserService userService;


    @Override
    public void likeOrDislike(CreateLikeDto createLikeDto) {
        if (isAlreadyLiked(userService.getActiveUser().getId(), createLikeDto.getTweetId())) {
            dislike(createLikeDto);
        } else {
            like(createLikeDto);
        }
    }

    @Override
    public LikeListingDto like(CreateLikeDto createLikeDto) {
        Like like = new Like();
        like.setUser(userService.getActiveUser());
        like.setTweet(tweetRepository.findById(createLikeDto.getTweetId())
                .orElseThrow(() -> new RuntimeException("Tweet not found")));
        like.setCreatedAt(LocalDateTime.now());

        return convertToListingDto(likeRepository.save(like));
    }

    @Override
    public void dislike(CreateLikeDto createLikeDto) {
        rules.checkIfLikeExists(createLikeDto.getTweetId(), userService.getActiveUser().getId());
        
        Like like = likeRepository.findByUserIdAndTweetId(
                userService.getActiveUser().getId(), 
                createLikeDto.getTweetId())
                .orElseThrow(() -> new RuntimeException("Like not found"));
        
        likeRepository.delete(like);
    }

    private LikeListingDto convertToListingDto(Like like) {
        LikeListingDto dto = new LikeListingDto();
        dto.setId(like.getId());
        
        UserListingDto userDto = new UserListingDto();
        userDto.setId(like.getUser().getId());
        userDto.setUsername(like.getUser().getUsername());
        userDto.setName(like.getUser().getName());
        userDto.setSurname(like.getUser().getSurname());
        dto.setUser(userDto);
        
        dto.setTweetId(like.getTweet().getId());
        return dto;
    }

    private Boolean isAlreadyLiked(UUID userId, Integer tweetId){
        return likeRepository.findByUserIdAndTweetId(userId, tweetId).isPresent();
    }
} 