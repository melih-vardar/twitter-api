package com.example.twitter.service;

import com.example.twitter.dto.tweet.UpdateTweetDto;
import com.example.twitter.rules.TweetBusinessRules;
import com.example.twitter.dto.tweet.CreateTweetDto;
import com.example.twitter.dto.tweet.TweetListingDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.dto.comment.CommentListingDto;
import com.example.twitter.dto.like.LikeListingDto;
import com.example.twitter.entity.Tweet;
import com.example.twitter.entity.User;
import com.example.twitter.repository.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TweetServiceImpl implements TweetService {
    private final TweetRepository tweetRepository;
    private final TweetBusinessRules tweetBusinessRules;
    private final UserService userService;

    @Override
    public CreateTweetDto create(CreateTweetDto createTweetDto) {
        Tweet tweet = new Tweet();
        tweet.setContent(createTweetDto.getContent());
        User user = userService.getActiveUser();
        tweet.setUser(user);
        tweet.setCreatedAt(LocalDateTime.now());

        tweetRepository.save(tweet);

        return convertToCreateDto(tweet);
    }

    @Override
    public List<TweetListingDto> findByUserId(UUID userId) {
        return tweetRepository
                .findByUserId(userId)
                .stream()
                .map(this::convertToListingDto)
                .collect(Collectors.toList());
    }

    @Override
    public TweetListingDto findById(Integer id) {
        tweetBusinessRules.checkIfTweetExists(id);
        return convertToListingDto(tweetRepository.findByTweetId(id));
    }

    @Override
    public CreateTweetDto update(Integer id, UpdateTweetDto updateTweetDto) {
        tweetBusinessRules.checkIfTweetExists(id);
        tweetBusinessRules.checkIfUserIsOwner(id);

        Tweet tweet = tweetRepository.findByTweetId(id);
        tweet.setContent(updateTweetDto.getContent());
        tweet.setUpdatedAt(LocalDateTime.now());

        tweetRepository.save(tweet);

        return convertToCreateDto(tweet);
    }

    @Override
    public void delete(Integer id) {
        tweetBusinessRules.checkIfTweetExists(id);
        tweetRepository.deleteById(id);
    }

    private CreateTweetDto convertToCreateDto(Tweet tweet) {
        CreateTweetDto dto = new CreateTweetDto();
        dto.setContent(tweet.getContent());
        return dto;
    }

    private TweetListingDto convertToListingDto(Tweet tweet) {
        TweetListingDto dto = new TweetListingDto();
        dto.setId(tweet.getId());
        dto.setContent(tweet.getContent());

        // User mapping
        UserListingDto userDto = new UserListingDto();
        userDto.setId(tweet.getUser().getId());
        userDto.setUsername(tweet.getUser().getUsername());
        userDto.setName(tweet.getUser().getName());
        userDto.setSurname(tweet.getUser().getSurname());

        dto.setUser(userDto);
        dto.setStartDate(tweet.getCreatedAt());

        // Comments mapping
        List<CommentListingDto> commentDtos = tweet.getComments().stream()
                .map(comment -> {
                    CommentListingDto commentDto = new CommentListingDto();
                    commentDto.setId(comment.getId());
                    commentDto.setContent(comment.getContent());
                    commentDto.setUser(userDto);
                    commentDto.setTweetId(comment.getTweet().getId());
                    return commentDto;
                })
                .collect(Collectors.toList());
        dto.setComments(commentDtos);

        // Likes mapping
        List<LikeListingDto> likeDtos = tweet.getLikes().stream()
                .map(like -> {
                    LikeListingDto likeDto = new LikeListingDto();
                    likeDto.setId(like.getId());
                    likeDto.setUser(userDto);
                    likeDto.setTweetId(like.getTweet().getId());
                    return likeDto;
                })
                .collect(Collectors.toList());
        dto.setLikes(likeDtos);

        return dto;
    }
} 