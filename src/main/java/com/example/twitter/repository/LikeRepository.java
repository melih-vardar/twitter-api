package com.example.twitter.repository;

import com.example.twitter.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface LikeRepository extends JpaRepository<Like, Integer> {
    Optional<Like> findByUserIdAndTweetId(UUID userId, Integer tweetId);
} 