package com.example.twitter.dto.tweet;

import com.example.twitter.dto.comment.CommentListingDto;
import com.example.twitter.dto.like.LikeListingDto;
import com.example.twitter.dto.user.UserListingDto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TweetListingDto {
    private Integer id;
    private String content;
    private UserListingDto user;
    private LocalDateTime startDate;
    private List<CommentListingDto> comments;
    private List<LikeListingDto> likes;
}
