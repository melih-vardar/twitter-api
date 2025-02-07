package com.example.twitter.controller;

import com.example.twitter.dto.AuthUserDto;
import com.example.twitter.dto.LoginDto;
import com.example.twitter.dto.RegisterDto;
import com.example.twitter.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UsersController {
    private final UserService userService;

    @PostMapping("register")
    public AuthUserDto register(@RequestBody @Valid RegisterDto registerDto) {
        return userService.register(registerDto);
    }

    @PostMapping("login")
    public AuthUserDto login(@RequestBody @Valid LoginDto loginDto) {
        return userService.login(loginDto);
    }
}
