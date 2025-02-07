package com.example.twitter.dto.comment;

import com.example.twitter.dto.user.UserListingDto;
import lombok.Data;

import java.util.UUID;

@Data
public class CommentListingDto {
    private Integer id;

    private String content;

    private UserListingDto user;

    private Integer tweetId;
}
