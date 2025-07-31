package com.demo.example.student_library_management_system.repository;

import com.demo.example.student_library_management_system.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT SUM(b.copies) FROM Book b WHERE b.copies > 0")
    Long countBooks();


    @Query("SELECT COUNT(b) from Book b where b.issuedToStudent=true")
    int countBorrowedBooks();


}
