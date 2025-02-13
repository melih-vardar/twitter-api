package com.example.twitter.controller;

import com.example.twitter.dto.comment.CommentListingDto;
import com.example.twitter.dto.comment.CreateCommentDto;
import com.example.twitter.dto.comment.UpdateCommentDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.service.CommentService;
import com.example.twitter.util.exception.type.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import com.example.twitter.config.TestSecurityConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommentController.class)
@Import(TestSecurityConfig.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setContent("Test comment");
        createCommentDto.setTweetId(1);

        CommentListingDto commentListingDto = new CommentListingDto();
        commentListingDto.setContent("Test comment");
        
        when(commentService.create(any(CreateCommentDto.class))).thenReturn(commentListingDto);

        mockMvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Test comment"));
    }

    @Test
    void findByTweetId() throws Exception {
        Integer tweetId = 1;
        List<CommentListingDto> comments = new ArrayList<>();
        CommentListingDto dto = new CommentListingDto();
        dto.setContent("Test comment");
        comments.add(dto);

        when(commentService.findByTweetId(tweetId)).thenReturn(comments);

        mockMvc.perform(get("/comment/byTweetId/{tweetId}", tweetId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test comment"));
    }

    @Test
    void update() throws Exception {
        Integer id = 1;
        UpdateCommentDto updateCommentDto = new UpdateCommentDto();
        updateCommentDto.setContent("Updated comment");

        CommentListingDto commentListingDto = new CommentListingDto();
        commentListingDto.setContent("Updated comment");

        when(commentService.update(eq(id), any(UpdateCommentDto.class))).thenReturn(commentListingDto);

        mockMvc.perform(put("/comment/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated comment"));
    }

    @Test
    void deleteTest() throws Exception {
        Integer id = 1;
        
        doNothing().when(commentService).delete(id);

        mockMvc.perform(delete("/comment/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void createWithEmptyContent() throws Exception {
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setTweetId(1);

        mockMvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors").isArray())
                .andExpect(jsonPath("$.errors[0]").value("Content cannot be blank"));
    }

    @Test
    void createWithTooLongContent() throws Exception {
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setTweetId(1);
        createCommentDto.setContent("a".repeat(281)); // 280<281

        mockMvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Comment must be less than 280 characters"));
    }

    @Test
    void createWithoutTweetId() throws Exception {
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setContent("Test comment");

        mockMvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Tweet ID is required"));
    }

    @Test
    void updateWithEmptyContent() throws Exception {
        Integer id = 1;
        UpdateCommentDto updateCommentDto = new UpdateCommentDto();

        mockMvc.perform(put("/comment/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Content cannot be blank"));
    }

    @Test
    void verifyCommentStructure() throws Exception {
        CreateCommentDto createCommentDto = new CreateCommentDto();
        createCommentDto.setContent("Test comment");
        createCommentDto.setTweetId(1);

        CommentListingDto commentListingDto = new CommentListingDto();
        commentListingDto.setId(1);
        commentListingDto.setContent("Test comment");
        commentListingDto.setTweetId(1);

        UserListingDto user = new UserListingDto();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        commentListingDto.setUser(user);

        when(commentService.create(any(CreateCommentDto.class))).thenReturn(commentListingDto);

        mockMvc.perform(post("/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createCommentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.content").exists())
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.id").exists())
                .andExpect(jsonPath("$.user.username").exists())
                .andExpect(jsonPath("$.tweetId").exists());
    }

    @Test
    void deleteNonExistentComment() throws Exception {
        Integer id = -1;
        doThrow(new BusinessException("Comment not found"))
            .when(commentService).delete(id);

        mockMvc.perform(delete("/comment/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("BusinessException"))
                .andExpect(jsonPath("$.message").value("Comment not found"));
    }

    @Test
    void updateUnauthorizedComment() throws Exception {
        Integer id = 1;
        UpdateCommentDto updateCommentDto = new UpdateCommentDto();
        updateCommentDto.setContent("Updated content");

        doThrow(new BusinessException("You are not authorized to edit this comment"))
            .when(commentService).update(eq(id), any(UpdateCommentDto.class));

        mockMvc.perform(put("/comment/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("BusinessException"))
                .andExpect(jsonPath("$.message").value("You are not authorized to edit this comment"));
    }
}