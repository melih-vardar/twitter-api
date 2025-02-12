package com.example.twitter.controller;

import com.example.twitter.dto.user.AuthUserDto;
import com.example.twitter.dto.user.LoginDto;
import com.example.twitter.dto.user.RegisterDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{username}")
    public UserListingDto getUserByUsername(@PathVariable String username) {
        return userService.findByUsername(username);
    }
}
