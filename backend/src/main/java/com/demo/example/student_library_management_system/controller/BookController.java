package com.demo.example.student_library_management_system.controller;

import com.demo.example.student_library_management_system.dto.BookRequestDto;
import com.demo.example.student_library_management_system.dto.StudentRequestDto;
import com.demo.example.student_library_management_system.model.Book;
import com.demo.example.student_library_management_system.model.Student;
import com.demo.example.student_library_management_system.service.BookService;
import com.demo.example.student_library_management_system.service.StudentService;
import com.demo.example.student_library_management_system.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/book/api")
public class BookController {

    @Autowired
    private BookService bookService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<?> saveBook(@RequestBody BookRequestDto bookRequestDto) {
        try {
            String savedBook = bookService.addBook(bookRequestDto);
            return ResponseEntity.ok("✅ Book saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();  // See actual error in logs
            return ResponseEntity.internalServerError().body("❌ Error: " + e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/update/{bookId}")
    public ResponseEntity<?> updateBook(@RequestBody BookRequestDto studentRequestDto,@PathVariable("bookId") int bookId){
        try {
            String response = bookService.updateBook(studentRequestDto, bookId);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @GetMapping("/findAll")
    public ResponseEntity<?> getAll(){
        try {
            List<Book> bookList = bookService.getAll();
            return ResponseEntity.ok().body(bookList);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{bookid}")
    public ResponseEntity<String> deleteStudentById(@PathVariable("bookid") int bookId){
        try {
            String response = bookService.deleteBookById(bookId);
            return ResponseEntity.ok("Book Deleted Successfully..!");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to delete book");
        }
    }

    @GetMapping("/totalBooks")
    public long totalBooks(){
        try {
            long totalBooks = bookService.countBooks();
            return totalBooks;
        }
        catch(Exception e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping("/borrowedBooks")
    public long totalBorrowed(){
        try{
            long totalBorrowed = bookService.borrowedBooks();
            return totalBorrowed;
        }catch(Exception e){
           throw new NoSuchElementException();
        }
    }




    }



