package com.example.twitter.repository;

import com.example.twitter.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RetweetRepository extends JpaRepository<Retweet, Integer> {

    @Query("SELECT r FROM Retweet r WHERE r.originalTweet.id = :originalTweetId AND r.user.id = :userId")
    Optional<Retweet> findByOriginalTweetIdAndUserId(@Param("originalTweetId") Integer originalTweetId, @Param("userId") UUID userId);
} 