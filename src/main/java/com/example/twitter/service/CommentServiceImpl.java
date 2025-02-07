package com.example.twitter.service;

import com.example.twitter.rules.CommentBusinessRules;
import com.example.twitter.dto.comment.CommentListingDto;
import com.example.twitter.dto.comment.CreateCommentDto;
import com.example.twitter.dto.comment.UpdateCommentDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.entity.Comment;
import com.example.twitter.repository.CommentRepository;
import com.example.twitter.repository.TweetRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final TweetRepository tweetRepository;
    private final CommentBusinessRules commentBusinessRules;
    private final UserService userService;

    @Override
    public CommentListingDto create(CreateCommentDto createCommentDto) {
        Comment comment = new Comment();
        comment.setContent(createCommentDto.getContent());
        comment.setUser(userService.getActiveUser());
        comment.setTweet(tweetRepository.findById(createCommentDto.getTweetId())
                .orElseThrow(() -> new RuntimeException("Tweet not found")));
        comment.setCreatedAt(LocalDateTime.now());

        return convertToListingDto(commentRepository.save(comment));
    }

    @Override
    public CommentListingDto update(Integer id, UpdateCommentDto updateCommentDto) {
        commentBusinessRules.checkIfCommentExists(id);
        commentBusinessRules.checkIfUserCanEditComment(id, userService.getActiveUser().getId());

        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        comment.setContent(updateCommentDto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        return convertToListingDto(commentRepository.save(comment));
    }

    @Override
    public void delete(Integer id) {
        commentBusinessRules.checkIfCommentExists(id);
        commentBusinessRules.checkIfUserCanDeleteComment(id, userService.getActiveUser().getId());
        commentRepository.deleteById(id);
    }

    @Override
    public List<CommentListingDto> findByTweetId(Integer tweetId) {
        return commentRepository.findByTweetId(tweetId)
                .stream()
                .map(this::convertToListingDto)
                .collect(Collectors.toList());
    }

    private CommentListingDto convertToListingDto(Comment comment) {
        CommentListingDto dto = new CommentListingDto();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());

        UserListingDto userDto = new UserListingDto();
        userDto.setId(comment.getUser().getId());
        userDto.setUsername(comment.getUser().getUsername());
        userDto.setName(comment.getUser().getName());
        userDto.setSurname(comment.getUser().getSurname());
        dto.setUser(userDto);

        dto.setTweetId(comment.getTweet().getId());
        return dto;
    }
} 