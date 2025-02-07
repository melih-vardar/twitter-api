package com.example.twitter.dto.like;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class CreateLikeDto {
    @NotNull(message = "Tweet ID is required")
    private Integer tweetId;
} 