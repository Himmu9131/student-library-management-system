package com.demo.example.student_library_management_system.converters;
import com.demo.example.student_library_management_system.dto.TransactionRequestDto;
import com.demo.example.student_library_management_system.model.Transaction;
import com.demo.example.student_library_management_system.response.TransactionResponseDto;

import java.time.LocalDate;

public class TransactionConverter {

    public static Transaction convertTransactionRequestDtoToTransaction(TransactionRequestDto transactionRequestDto){
        Transaction transaction=Transaction.builder().transactionStatus(transactionRequestDto.getTransactionStatus())
                .isIssueOperation(transactionRequestDto.isIssueOperation())
                .fine(transactionRequestDto.getFine()).build();
        return transaction;
    }

    public static TransactionResponseDto convertToDto(Transaction t) {

        TransactionResponseDto transaction=TransactionResponseDto.builder()
                .id(t.getId()).studentName(t.getBook().getName())
                .bookName(t.getCard().getStudent().getName())
                .cardId(t.getCard().getId())
                .isIssueOperation(t.isIssueOperation())
                .transactionStatus(t.getTransactionStatus().name())
                .fine(t.getFine()).transactionDate(t.getTransactionDate())
                .build();
        return transaction;
    }


}
