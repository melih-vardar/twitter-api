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
import com.example.twitter.util.exception.type.BusinessException;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasItems;

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

    @Test
    void registerWithInvalidEmail() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("invalid-email");
        registerDto.setPassword("Test1234");
        registerDto.setName("Test");
        registerDto.setSurname("Test");

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]").value("Invalid email format"));
    }

    @Test
    void registerWithWeakPassword() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("test@test.com");
        registerDto.setPassword("weak");
        registerDto.setName("Test");
        registerDto.setSurname("Test");

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("ValidationError"))
                .andExpect(jsonPath("$.errors", hasSize(3)))
                .andExpect(jsonPath("$.errors", hasItems(
                    "Password must be at least 8 characters",
                    "Password must contain at least one number",
                    "Password must contain at least one uppercase letter"
                )));
    }

    @Test
    void loginWithInvalidCredentials() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("test@test.com");
        loginDto.setPassword("wrongpassword");

        when(userService.login(any(LoginDto.class)))
            .thenThrow(new BusinessException("Invalid or wrong credentials."));

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("BusinessException"))
                .andExpect(jsonPath("$.message").value("Invalid or wrong credentials."));
    }

    @Test
    void registerWithExistingUsername() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setUsername("existing@test.com");
        registerDto.setPassword("Test1234");
        registerDto.setName("Test");
        registerDto.setSurname("Test");

        when(userService.register(any(RegisterDto.class)))
            .thenThrow(new BusinessException("Username already exists."));

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("BusinessException"))
                .andExpect(jsonPath("$.message").value("Username already exists."));
    }

    @Test
    void getUserByNonExistentUsername() throws Exception {
        String username = "nonexistent";
        when(userService.findByUsername(username))
            .thenThrow(new BusinessException("User not found"));

        mockMvc.perform(get("/users/{username}", username))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("BusinessException"))
                .andExpect(jsonPath("$.message").value("User not found"));
    }

    @Test
    void verifyUserStructure() throws Exception {
        String username = "testuser";
        UserListingDto userListingDto = new UserListingDto();
        userListingDto.setId(UUID.randomUUID());
        userListingDto.setUsername(username);
        userListingDto.setName("Test");
        userListingDto.setSurname("User");

        when(userService.findByUsername(username)).thenReturn(userListingDto);

        mockMvc.perform(get("/users/{username}", username)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.surname").exists());
    }
}