package com.example.twitter.util.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3200"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((req) -> req
                        .requestMatchers(HttpMethod.POST, "/users/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/tweet/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/comment/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/retweet/**").permitAll()
                        
                        .requestMatchers(HttpMethod.POST, "/tweet").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/tweet/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/tweet/**").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/comment").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/comment/**").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/comment/**").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/like").authenticated()
                        
                        .requestMatchers(HttpMethod.POST, "/retweet").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/retweet/**").authenticated()
                        
                        .anyRequest().permitAll()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}