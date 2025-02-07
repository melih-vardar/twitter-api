package com.example.twitter.dto.user;

import lombok.Data;

import java.util.UUID;

@Data
public class UserListingDto {
    private UUID id;
    private String username;
    private String name;
    private String surname;

}
