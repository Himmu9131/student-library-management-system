package com.demo.example.student_library_management_system.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
@Data
@AllArgsConstructor
public class BookAuthorAndStudentsDto {
    private String name;
    private List<StudentResponseDto> studentResponseDtoList;
}
