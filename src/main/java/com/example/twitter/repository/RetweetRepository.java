package com.example.twitter.repository;

import com.example.twitter.entity.Retweet;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RetweetRepository extends JpaRepository<Retweet, Integer> {
    List<Retweet> findByOriginalTweetId(Integer tweetId);
} 