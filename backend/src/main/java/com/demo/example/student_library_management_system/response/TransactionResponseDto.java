package com.demo.example.student_library_management_system.response;

import com.demo.example.student_library_management_system.enums.TransactionStatus;
import lombok.*;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDto {
    private int id;
    private String bookName;
    private String studentName;
    private int cardId;
    private boolean isIssueOperation;
    private String transactionStatus;
    private double fine;
    private Date transactionDate;

    public TransactionResponseDto(int id, String bookName, String studentName, int cardId,
                                  boolean issueOperation, TransactionStatus transactionStatus,
                                  double fine, Date transactionDate) {
        this.id = id;
        this.bookName = bookName;
        this.studentName = studentName;
        this.cardId = cardId;
        this.isIssueOperation = issueOperation;
        this.transactionStatus = transactionStatus.name(); // enum to string
        this.fine = fine;
        this.transactionDate = transactionDate;
    }
}

