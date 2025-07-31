package com.demo.example.student_library_management_system.dto;

import com.demo.example.student_library_management_system.enums.TransactionStatus;
import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
public class TransactionRequestDto {

    private TransactionStatus transactionStatus;
    private double fine;
    private boolean issueOperation;
    private int bookId;
    private int cardId;
}
