package com.demo.example.student_library_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDashboardDto {
    private String studentName;
    private int booksBorrowed;
    private double totalFine;
    private List<BorrowedBookDto> currentBorrowedBooks;
    private String welcomeMessage;
    private int overdueBooks;



    public StudentDashboardDto(int booksBorrowed, double totalFine, int overdueBooks, String welcomeMessage) {
        this.booksBorrowed = booksBorrowed;
        this.welcomeMessage = welcomeMessage;
       this.totalFine=totalFine;
        this.overdueBooks=overdueBooks;

    }
}


