package com.example.twitter.repository;

import com.example.twitter.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findByTweetId(Integer tweetId);
}