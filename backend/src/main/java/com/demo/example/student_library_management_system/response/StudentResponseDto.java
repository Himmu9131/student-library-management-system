package com.demo.example.student_library_management_system.response;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDto {
    private int id;
    private String name;
    private String email;
    private int age;
    private String address;
    private String mobile;
    private int cardId;
}
