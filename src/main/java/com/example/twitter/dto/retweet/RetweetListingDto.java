package com.example.twitter.dto.retweet;

import com.example.twitter.dto.user.UserListingDto;
import lombok.Data;

@Data
public class RetweetListingDto {
    private Integer id;
    private Integer originalTweetId;
    private UserListingDto user;
    private String quote;
}
