package com.example.sports.dto;

import lombok.Data;

@Data
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private String role;  // SUPER_ADMIN / COACH / USER
}