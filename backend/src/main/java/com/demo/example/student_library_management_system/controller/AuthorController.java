package com.demo.example.student_library_management_system.controller;

import com.demo.example.student_library_management_system.dto.AuthorRequestDto;
import com.demo.example.student_library_management_system.dto.StudentRequestDto;
import com.demo.example.student_library_management_system.model.Author;
import com.demo.example.student_library_management_system.model.Transaction;
import com.demo.example.student_library_management_system.service.AuthorService;
import com.demo.example.student_library_management_system.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/author/api")
public class AuthorController {

    @Autowired
    private AuthorService authorService;
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public String saveAuthor(@RequestBody AuthorRequestDto authorRequestDto){
        String response = authorService.addAuthor(authorRequestDto);
        return response;
    }
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    @GetMapping("/findAll")
    public ResponseEntity<?> getAllTransactions(){
        try {
            List<Author> authorList = authorService.getAll();
            return ResponseEntity.ok().body(authorList);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
