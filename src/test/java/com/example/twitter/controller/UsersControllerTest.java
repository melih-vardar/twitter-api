package com.example.twitter.controller;

import com.example.twitter.dto.user.AuthUserDto;
import com.example.twitter.dto.user.RegisterDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.context.annotation.Import;
import com.example.twitter.config.TestSecurityConfig;
import com.example.twitter.dto.user.LoginDto;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UsersController.class)
@Import(TestSecurityConfig.class)
class UsersControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void register() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("testuser@test.com");
        registerDto.setPassword("Test1234");
        registerDto.setName("Test");
        registerDto.setSurname("Test");


        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken("test-token");

        when(userService.register(any(RegisterDto.class))).thenReturn(authUserDto);

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("test-token"));
    }

    @Test
    void getUserByUsername() throws Exception {
        String username = "testuser";
        UserListingDto userListingDto = new UserListingDto();
        userListingDto.setUsername(username);

        when(userService.findByUsername(username)).thenReturn(userListingDto);

        mockMvc.perform(get("/users/{username}", username)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(username));
    }

    @Test
    void login() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testuser@test.com");
        loginDto.setPassword("Test1234");

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken("test-token");

        when(userService.login(any(LoginDto.class))).thenReturn(authUserDto);

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }
}