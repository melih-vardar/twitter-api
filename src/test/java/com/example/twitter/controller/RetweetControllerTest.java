package com.example.twitter.controller;

import com.example.twitter.dto.retweet.CreateRetweetDto;
import com.example.twitter.dto.retweet.RetweetListingDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.service.RetweetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Import;
import com.example.twitter.config.TestSecurityConfig;
import com.example.twitter.util.exception.type.BusinessException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = RetweetController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(TestSecurityConfig.class)
class RetweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetweetService retweetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        CreateRetweetDto createRetweetDto = new CreateRetweetDto();
        createRetweetDto.setOriginalTweetId(1);
        createRetweetDto.setQuote("Random quote");

        RetweetListingDto retweetListingDto = new RetweetListingDto();
        retweetListingDto.setOriginalTweetId(1);

        when(retweetService.create(any(CreateRetweetDto.class))).thenReturn(retweetListingDto);

        mockMvc.perform(post("/retweet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRetweetDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void getCurrentUserRetweets() throws Exception {
        List<RetweetListingDto> retweets = new ArrayList<>();
        RetweetListingDto dto = new RetweetListingDto();
        dto.setOriginalTweetId(1);
        retweets.add(dto);

        when(retweetService.getCurrentUserRetweets()).thenReturn(retweets);

        mockMvc.perform(get("/retweet/my-retweets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].originalTweetId").value(1));
    }

    @Test
    void deleteTest() throws Exception {
        Integer id = 1;
        
        doNothing().when(retweetService).delete(id);

        mockMvc.perform(delete("/retweet/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void getRetweetsByUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        List<RetweetListingDto> retweets = new ArrayList<>();
        RetweetListingDto retweet = new RetweetListingDto();
        retweet.setOriginalTweetId(1);
        retweets.add(retweet);

        when(retweetService.getRetweetsByUserId(userId)).thenReturn(retweets);

        mockMvc.perform(get("/retweet/findByUserId")
                .param("userId", userId.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].originalTweetId").value(1));
    }

    @Test
    void createWithEmptyQuote() throws Exception {
        CreateRetweetDto createRetweetDto = new CreateRetweetDto();
        createRetweetDto.setOriginalTweetId(1);

        mockMvc.perform(post("/retweet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRetweetDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Quote cannot be blank"));
    }

    @Test
    void createWithTooLongQuote() throws Exception {
        CreateRetweetDto createRetweetDto = new CreateRetweetDto();
        createRetweetDto.setOriginalTweetId(1);
        createRetweetDto.setQuote("a".repeat(281));

        mockMvc.perform(post("/retweet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRetweetDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Quote must be less than 280 characters"));
    }

    @Test
    void createWithoutOriginalTweetId() throws Exception {
        CreateRetweetDto createRetweetDto = new CreateRetweetDto();
        createRetweetDto.setQuote("Test quote");

        mockMvc.perform(post("/retweet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRetweetDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Original Tweet ID is required"));
    }

    @Test
    void verifyRetweetStructure() throws Exception {
        CreateRetweetDto createRetweetDto = new CreateRetweetDto();
        createRetweetDto.setOriginalTweetId(1);
        createRetweetDto.setQuote("Test quote");

        RetweetListingDto retweetListingDto = new RetweetListingDto();
        retweetListingDto.setId(1);
        retweetListingDto.setOriginalTweetId(1);
        retweetListingDto.setQuote("Test quote");
        retweetListingDto.setStartDate(LocalDateTime.now());
        UserListingDto user = new UserListingDto();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        retweetListingDto.setUser(user);

        when(retweetService.create(any(CreateRetweetDto.class))).thenReturn(retweetListingDto);

        mockMvc.perform(post("/retweet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRetweetDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.originalTweetId").exists())
                .andExpect(jsonPath("$.quote").exists())
                .andExpect(jsonPath("$.startDate").exists())
                .andExpect(jsonPath("$.user").exists())
                .andExpect(jsonPath("$.user.id").exists())
                .andExpect(jsonPath("$.user.username").exists());
    }

    @Test
    void deleteNonExistentRetweet() throws Exception {
        Integer id = -1;
        doThrow(new BusinessException("Retweet not found"))
            .when(retweetService).delete(id);

        mockMvc.perform(delete("/retweet/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("BusinessException"))
                .andExpect(jsonPath("$.message").value("Retweet not found"));
    }

    @Test
    void findRetweetsByInvalidUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        when(retweetService.getRetweetsByUserId(userId))
            .thenThrow(new BusinessException("User not found"));

        mockMvc.perform(get("/retweet/findByUserId")
                .param("userId", userId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("BusinessException"))
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void deleteUnauthorizedRetweet() throws Exception {
        Integer id = 1;
        doThrow(new BusinessException("You are not authorized to perform this action"))
            .when(retweetService).delete(id);

        mockMvc.perform(delete("/retweet/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("BusinessException"))
                .andExpect(jsonPath("$.message").value("You are not authorized to perform this action"));
    }
}