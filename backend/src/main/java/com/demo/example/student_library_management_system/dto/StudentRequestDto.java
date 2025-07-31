package com.demo.example.student_library_management_system.dto;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class StudentRequestDto {
    // here we take input parameters which are going as request

    private String name;
    private String email;
    private int age;
    private String address;
    private String mobile;
}
