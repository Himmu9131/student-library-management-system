package com.demo.example.student_library_management_system.model;

import com.demo.example.student_library_management_system.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Data
@Entity
@Table(name = "transaction")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "transaction_status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private TransactionStatus transactionStatus;

    @Column(nullable = false)
    private double fine;

    @Column(name = "transaction_date", nullable = false)
    @CreationTimestamp
    private Date transactionDate;

    @Column(name = "is_issue_operation", nullable = false)
    private boolean isIssueOperation;

    @JsonBackReference
    @ManyToOne
    @JoinColumn
    private Card card;

    @JsonBackReference
    @ManyToOne
    @JoinColumn
    private Book book;

}
