package com.example.twitter.service;

import com.example.twitter.dto.user.AuthUserDto;
import com.example.twitter.dto.user.LoginDto;
import com.example.twitter.dto.user.RegisterDto;
import com.example.twitter.dto.user.UserListingDto;
import com.example.twitter.entity.User;

import java.util.UUID;

public interface UserService {
    AuthUserDto register(RegisterDto registerDto);
    AuthUserDto login(LoginDto loginDto);
    User getActiveUser();
    UUID getActiveUserId();
    UserListingDto findByUsername(String username);
}
