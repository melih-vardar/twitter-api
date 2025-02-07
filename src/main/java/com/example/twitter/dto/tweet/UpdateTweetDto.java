package com.example.twitter.dto.tweet;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateTweetDto {
    @NotBlank(message = "Content cannot be blank")
    @Size(max = 280, message = "Tweet must include less than 280 characters")
    private String content;
}
