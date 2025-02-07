package com.example.twitter.repository;

import com.example.twitter.entity.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TweetRepository extends JpaRepository<Tweet, Integer> {
    List<Tweet> findByUserId(UUID userId);
}