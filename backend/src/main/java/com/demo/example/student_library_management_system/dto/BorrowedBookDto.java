package com.demo.example.student_library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BorrowedBookDto {
    private String bookName;
    private Date issuedDate;
    private Date dueDate;
    private boolean isOverdue;
}

