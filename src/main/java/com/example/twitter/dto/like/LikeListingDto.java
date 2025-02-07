package com.example.twitter.dto.like;

import com.example.twitter.dto.user.UserListingDto;
import lombok.Data;

@Data
public class LikeListingDto {
    private Integer id;
    private UserListingDto user;
    private Integer tweetId;
}
