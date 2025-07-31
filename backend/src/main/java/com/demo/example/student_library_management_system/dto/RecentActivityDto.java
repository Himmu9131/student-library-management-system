package com.demo.example.student_library_management_system.dto;

import com.demo.example.student_library_management_system.enums.TransactionStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Arrays;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class RecentActivityDto {
    private String studentName;
    private String bookName;
    private boolean isIssueOperation;

    public RecentActivityDto(String studentName, String bookName, boolean isIssueOperation) {
        this.studentName = capitalizeWords(studentName);
        this.bookName = capitalizeWords(bookName);
        this.isIssueOperation = isIssueOperation; // enum to string
    }

    private String capitalizeWords(String input) {
        return Arrays.stream(input.trim().split(" "))
                .map(word -> word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase())
                .collect(Collectors.joining(" "));
    }
}


