package com.example.oauthPractice.jwt;

import lombok.Data;

@Data
public class JwtRequestDto {
    private String username;
    private String password;
    private String email;
}