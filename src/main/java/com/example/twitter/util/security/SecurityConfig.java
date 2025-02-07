package com.example.twitter.util.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
//                        .requestMatchers(HttpMethod.DELETE,"/api/v1/products/**").hasAnyAuthority("Product.Delete", "Admin")
//                        .requestMatchers(HttpMethod.POST,"/api/v1/products/**").hasAnyAuthority("Product.Create", "Admin")
//                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyAuthority("Product.Update", "Admin")
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}