package com.example.twitter.util.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((req) -> req

                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tweet/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comment/**").permitAll()
                        
                        .requestMatchers(HttpMethod.POST, "/tweet").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/tweet/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/tweet/**").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/comment").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/comment/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/comment/**").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/like").authenticated()
                        .requestMatchers(HttpMethod.POST, "/dislike").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/retweet").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/retweet/**").authenticated()
                        
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}