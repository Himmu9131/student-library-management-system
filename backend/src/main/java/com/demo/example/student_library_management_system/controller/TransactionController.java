package com.demo.example.student_library_management_system.controller;


import com.demo.example.student_library_management_system.dto.BorrowedBookDto;
import com.demo.example.student_library_management_system.dto.RecentActivityDto;
import com.demo.example.student_library_management_system.dto.StudentRequestDto;
import com.demo.example.student_library_management_system.dto.TransactionRequestDto;
import com.demo.example.student_library_management_system.model.Student;
import com.demo.example.student_library_management_system.model.Transaction;
import com.demo.example.student_library_management_system.response.TransactionResponseDto;
import com.demo.example.student_library_management_system.service.RecentActivityService;
import com.demo.example.student_library_management_system.service.StudentService;
import com.demo.example.student_library_management_system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transaction/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private RecentActivityService recentActivityService;

    @PostMapping("/save")
    public String saveTransaction(@RequestBody TransactionRequestDto transactionRequestDto){
        String response = transactionService.addTransaction(transactionRequestDto);
        return response;
    }
    @GetMapping("/findAll")
    public ResponseEntity<?> getAll(){
        try {
            List<TransactionResponseDto> transactionList = transactionService.getAll();
            return ResponseEntity.ok().body(transactionList);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @GetMapping("/overdueTransactions")
    public ResponseEntity<?> overdueTransactions(){
        try {
            int overdue = transactionService.overDueTransactions().size();
            return ResponseEntity.ok(overdue);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(0);
        }
    }



    // ✅ Issue Book
    @PostMapping("/issue")
    public ResponseEntity<Transaction> issueBook(
            @RequestParam int bookId,
            @RequestParam int cardId) {

        Transaction transaction = transactionService.issueBook(bookId, cardId);
        return ResponseEntity.ok(transaction);
    }

    // ✅ Return Book
    @PostMapping("/return")
    public ResponseEntity<Transaction> returnBook(
            @RequestParam int bookId,
            @RequestParam int cardId) {

        Transaction transaction = transactionService.returnBook(bookId, cardId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/popular-books")
    public ResponseEntity<?> popularBooks() {
        try {
            List<String> popularBooks = transactionService.findMostPopularBooks(); // Already an int
            System.out.println("popularBooks() called");
            return ResponseEntity.ok(popularBooks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @GetMapping("/borrowed-books/{studentId}")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<List<BorrowedBookDto>> getBorrowedBooks(@PathVariable int studentId) {
        List<BorrowedBookDto> borrowedBooks = transactionService.getBorrowedBooksByStudentId(studentId);
        return ResponseEntity.ok(borrowedBooks);
    }


    @GetMapping("/recent-activity")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RecentActivityDto>> getRecentActivity(){
        List<RecentActivityDto> recentActivityDtoList=recentActivityService.getRecentActivities();
        return ResponseEntity.ok(recentActivityDtoList);
    }
    @GetMapping("/totalBorrowers")
    public ResponseEntity<Integer> totalBorrowers() {
        try {
            int totalBorrowers = transactionService.totalBorrower(); // Already an int
            System.out.println("totalBorrowers() called");
            return ResponseEntity.ok(totalBorrowers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(0);
        }
    }



}
