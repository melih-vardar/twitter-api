package com.example.twitter.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.UUID;

@Data
public class CreateCommentDto {
    @NotBlank(message = "Content cannot be blank")
    @Size(max = 280, message = "Comment must be less than 280 characters")
    private String content;

    @NotNull(message = "Tweet ID is required")
    private Integer tweetId;
} 