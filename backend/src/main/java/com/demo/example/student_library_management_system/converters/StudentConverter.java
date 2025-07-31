package com.demo.example.student_library_management_system.converters;

import com.demo.example.student_library_management_system.dto.StudentRequestDto;
import com.demo.example.student_library_management_system.model.Student;
import com.demo.example.student_library_management_system.response.StudentResponseDto;

public class StudentConverter {
    // converts the StudentRequestDto into student model class

    public static Student convertStudentRequestDtoToStudent(StudentRequestDto studentRequestDto){
        //builder - helps to build the object and set it values
        Student student = Student.builder().name(studentRequestDto.getName()).age(studentRequestDto.getAge())
                .email(studentRequestDto.getEmail()).mobile(studentRequestDto.getMobile())
                .address(studentRequestDto.getAddress())
                .build();
        return student;
    }

    public static StudentResponseDto convertToDto(Student student) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId());
        dto.setName(student.getName());
        dto.setEmail(student.getEmail());
        dto.setAge(student.getAge());
        dto.setAddress(student.getAddress());
        dto.setMobile(student.getMobile());
        if(student.getCard()!=null){
            dto.setCardId(student.getCard().getId());
        }
        return dto;
    }
}
