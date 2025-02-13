package com.example.twitter.controller;

import com.example.twitter.dto.comment.CommentListingDto;
import com.example.twitter.dto.comment.CreateCommentDto;
import com.example.twitter.dto.comment.UpdateCommentDto;
import com.example.twitter.service.CommentService;
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
}