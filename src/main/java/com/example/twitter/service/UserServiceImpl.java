package com.example.twitter.service;

import com.example.twitter.dto.AuthUserDto;
import com.example.twitter.dto.LoginDto;
import com.example.twitter.dto.RegisterDto;
import com.example.twitter.entity.User;
import com.example.twitter.repository.UserRepository;
import com.example.twitter.rules.UserBusinessRules;
import com.example.twitter.util.exception.type.BusinessException;
import com.example.twitter.util.jwt.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserBusinessRules userBusinessRules;
    private final JwtService jwtService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public AuthUserDto register(RegisterDto registerDto) {
        userBusinessRules.usernameMustNotExist(registerDto.getUsername());

        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setName(registerDto.getName());
        user.setSurname(registerDto.getSurname());

        userRepository.save(user);

        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken(this.jwtService.generateToken(user.getUsername()));

        return authUserDto;
    }

    @Override
    public AuthUserDto login(LoginDto loginDto) {
        User dbUser = userRepository.findByUsername(loginDto.getUsername())
                .orElseThrow(() -> new BusinessException("Invalid or wrong credentials"));

        boolean isPasswordCorrect = passwordEncoder
                .matches(loginDto.getPassword(), dbUser.getPassword());

        if(!isPasswordCorrect){
            throw new BusinessException("Invalid or wrong credentials");
        }
        AuthUserDto authUserDto = new AuthUserDto();
        authUserDto.setToken(this.jwtService.generateToken(dbUser.getUsername()));

        return authUserDto;

    }
}
