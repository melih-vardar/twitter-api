package com.example.twitter.controller;

import com.example.twitter.dto.like.CreateLikeDto;
import com.example.twitter.service.LikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import com.example.twitter.config.TestSecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LikeController.class)
@Import(TestSecurityConfig.class)
class LikeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void likeOrDislike() throws Exception {
        CreateLikeDto createLikeDto = new CreateLikeDto();
        createLikeDto.setTweetId(1);

        doNothing().when(likeService).likeOrDislike(any(CreateLikeDto.class));

        mockMvc.perform(post("/like")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createLikeDto)))
                .andExpect(status().isOk());
    }
}