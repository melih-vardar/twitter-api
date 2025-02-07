package com.example.twitter.controller;

import com.example.twitter.dto.comment.CommentListingDto;
import com.example.twitter.dto.comment.CreateCommentDto;
import com.example.twitter.dto.comment.UpdateCommentDto;
import com.example.twitter.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public CommentListingDto create(@Valid @RequestBody CreateCommentDto createCommentDto) {
        return commentService.create(createCommentDto);
    }

    @PutMapping("/{id}")
    public CommentListingDto update(@PathVariable Integer id, @Valid @RequestBody UpdateCommentDto updateCommentDto) {
        return commentService.update(id, updateCommentDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        commentService.delete(id);
    }

    @GetMapping("/byTweetId/{tweetId}")
    public List<CommentListingDto> findByTweetId(@PathVariable Integer tweetId) {
        return commentService.findByTweetId(tweetId);
    }
} 