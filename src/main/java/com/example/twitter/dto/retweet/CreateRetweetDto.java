package com.example.twitter.dto.retweet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateRetweetDto {
    @NotNull(message = "Original Tweet ID is required")
    private Integer originalTweetId;
    
    @NotBlank(message = "Quote cannot be blank")
    @Size(max = 280, message = "Quote must be less than 280 characters")
    private String quote;
} 