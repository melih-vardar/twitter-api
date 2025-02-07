package com.example.twitter.service;

import com.example.twitter.dto.AuthUserDto;
import com.example.twitter.dto.LoginDto;
import com.example.twitter.dto.RegisterDto;

public interface UserService {
    AuthUserDto register(RegisterDto registerDto);
    AuthUserDto login(LoginDto loginDto);
}
