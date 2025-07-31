package com.demo.example.student_library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@AllArgsConstructor
public class UpdatePasswordRequestDto {
    private String email;
    private String newPass;
    private String confirmPass;

    // Getters and Setters or use Lombok @Data
}
