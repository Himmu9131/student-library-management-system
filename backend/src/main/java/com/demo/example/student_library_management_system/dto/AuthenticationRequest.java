package com.demo.example.student_library_management_system.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}

