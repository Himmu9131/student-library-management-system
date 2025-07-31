package com.demo.example.student_library_management_system.service;

import com.demo.example.student_library_management_system.dto.RecentActivityDto;
import com.demo.example.student_library_management_system.model.Transaction;
import com.demo.example.student_library_management_system.repository.TransactionRepository;
import com.demo.example.student_library_management_system.response.TransactionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecentActivityService {

    private final TransactionRepository transactionRepository;

//    public List<Transaction> getRecentActivities() {
//        List<Transaction> transactions = transactionRepository.findTop4ByOrderByTransactionDateDesc();
//
//        return transactions.stream()
//                .map(t -> new TransactionResponseDto(
//                        t.getId(),
//                        t.getBook().getName(),
//                        t.getCard().getStudent().getName(),
//                        t.getCard().getId(),
//                        t.isIssueOperation(),
//                        t.getTransactionStatus().name(), // convert enum to String
//                        t.getFine(),
//                        new Timestamp(t.getTransactionDate().getTime())
//                ))
//                .collect(Collectors.toList());
//    }

    public List<RecentActivityDto> getRecentActivities() {
        Pageable top5 = PageRequest.of(0, 5); // offset = 0, limit = 3
        return transactionRepository.findRecentActivities(top5);
    }


}

