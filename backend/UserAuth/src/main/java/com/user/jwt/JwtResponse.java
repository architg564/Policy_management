package com.user.jwt;

import lombok.Data;

@Data
public class JwtResponse {
    private String token;
    private String role;
}
