package com.example.twitter.controller;

import com.example.twitter.dto.tweet.CreateTweetDto;
import com.example.twitter.dto.tweet.TweetListingDto;
import com.example.twitter.service.TweetService;
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
import com.example.twitter.dto.tweet.UpdateTweetDto;
import java.util.UUID;
import java.util.List;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = TweetController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(TestSecurityConfig.class)
class TweetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TweetService tweetService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void create() throws Exception {
        CreateTweetDto createTweetDto = new CreateTweetDto();
        createTweetDto.setContent("Test tweet");

        when(tweetService.create(any(CreateTweetDto.class))).thenReturn(createTweetDto);

        mockMvc.perform(post("/tweet")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createTweetDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value("Test tweet"));
    }

    @Test
    void findById() throws Exception {
        Integer id = 1;
        TweetListingDto tweetListingDto = new TweetListingDto();
        tweetListingDto.setContent("Test tweet");

        when(tweetService.findById(id)).thenReturn(tweetListingDto);

        mockMvc.perform(get("/tweet/findById")
                .param("id", id.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Test tweet"));
    }

    @Test
    void update() throws Exception {
        Integer id = 1;
        UpdateTweetDto updateTweetDto = new UpdateTweetDto();
        updateTweetDto.setContent("Updated tweet");

        CreateTweetDto updatedTweet = new CreateTweetDto();
        updatedTweet.setContent("Updated tweet");

        when(tweetService.update(eq(id), any(UpdateTweetDto.class))).thenReturn(updatedTweet);

        mockMvc.perform(put("/tweet/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateTweetDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("Updated tweet"));
    }

    @Test
    void deleteTest() throws Exception {
        Integer id = 1;
        
        doNothing().when(tweetService).delete(id);

        mockMvc.perform(delete("/tweet/{id}", id))
                .andExpect(status().isNoContent());
    }

    @Test
    void findByUserId() throws Exception {
        UUID userId = UUID.randomUUID();
        List<TweetListingDto> tweets = new ArrayList<>();
        TweetListingDto tweet = new TweetListingDto();
        tweet.setContent("Test tweet");
        tweets.add(tweet);

        when(tweetService.findByUserId(userId)).thenReturn(tweets);

        mockMvc.perform(get("/tweet/findByUserId")
                .param("userId", userId.toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test tweet"));
    }

    @Test
    void getCurrentUserTweets() throws Exception {
        List<TweetListingDto> tweets = new ArrayList<>();
        TweetListingDto tweet = new TweetListingDto();
        tweet.setContent("Test tweet");
        tweets.add(tweet);

        when(tweetService.findCurrentUserTweets()).thenReturn(tweets);

        mockMvc.perform(get("/tweet/my-tweets")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Test tweet"));
    }
}