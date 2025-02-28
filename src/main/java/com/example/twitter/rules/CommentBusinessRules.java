package com.example.twitter.rules;

import com.example.twitter.entity.Comment;
import com.example.twitter.repository.CommentRepository;
import com.example.twitter.util.exception.type.BusinessException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CommentBusinessRules {
    private final CommentRepository commentRepository;

    public void checkIfCommentExists(Integer id) {
        if (!commentRepository.existsById(id)) {
            throw new BusinessException("Comment not found");
        }
    }

    public void checkIfUserCanDeleteComment(Integer commentId, UUID userId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new BusinessException("Comment not found"));
        
        if (!comment.getUser().getId().equals(userId) && 
            !comment.getTweet().getUser().getId().equals(userId)) {
            throw new BusinessException("You are not authorized to delete this comment");
        }
    }

    public void checkIfUserCanEditComment(Integer commentId, UUID userId) {
        Comment comment = commentRepository.findById(commentId)
            .orElseThrow(() -> new BusinessException("Comment not found"));

        if (!comment.getUser().getId().equals(userId)) {
            throw new BusinessException("You are not authorized to edit this comment");
        }
    }
} 