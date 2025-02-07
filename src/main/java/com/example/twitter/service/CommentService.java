package com.example.twitter.service;

import com.example.twitter.dto.comment.CommentListingDto;
import com.example.twitter.dto.comment.CreateCommentDto;
import com.example.twitter.dto.comment.UpdateCommentDto;
import java.util.List;

public interface CommentService {
    CommentListingDto create(CreateCommentDto createCommentDto);
    CommentListingDto update(Integer id, UpdateCommentDto updateCommentDto);
    void delete(Integer id);
    List<CommentListingDto> findByTweetId(Integer tweetId);
} 