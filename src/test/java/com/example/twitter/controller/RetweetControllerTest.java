package com.example.twitter.controller;

import com.example.twitter.dto.retweet.CreateRetweetDto;
import com.example.twitter.dto.retweet.RetweetListingDto;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
}