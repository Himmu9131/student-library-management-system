package com.demo.example.student_library_management_system.service;

import com.demo.example.student_library_management_system.converters.BookConverter;
import com.demo.example.student_library_management_system.converters.StudentConverter;
import com.demo.example.student_library_management_system.dto.BookRequestDto;
import com.demo.example.student_library_management_system.dto.BorrowedBookDto;
import com.demo.example.student_library_management_system.dto.CardRequestDto;
import com.demo.example.student_library_management_system.dto.StudentRequestDto;
import com.demo.example.student_library_management_system.model.*;
import com.demo.example.student_library_management_system.repository.AuthorRepository;
import com.demo.example.student_library_management_system.repository.BookRepository;
import com.demo.example.student_library_management_system.repository.CardRepository;
import com.demo.example.student_library_management_system.response.BookAuthorAndStudentsDto;
import com.demo.example.student_library_management_system.response.StudentResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public String addBook(BookRequestDto bookRequestDto) {

        Book book = BookConverter.convertBookRequestDtoToBook(bookRequestDto);

        Author author = authorRepository.findById(bookRequestDto.getAuthorId()).get();
        book.setAuthor(author);

        // ✅ Maintain both sides of the relationship
        author.getBookByAuthor().add(book);

        bookRepository.save(book);

        return "Book save successfully";

    }

    public List<Book> getAll() {
        List<Book> bookList = bookRepository.findAll();
        if (bookList.isEmpty()) {
            throw new RuntimeException("Books are not present");
        }

        return bookList;
    }

    public String deleteBookById(int bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new RuntimeException("❌ Student with ID " + bookId + " does not exist.");
        }
        bookRepository.deleteById(bookId);
        return "book got deleted";
    }

    public long countBooks() {
        return bookRepository.countBooks();
    }

    public long borrowedBooks() {
        return bookRepository.countBorrowedBooks();
    }




    public String updateBook(BookRequestDto bookRequestDto, int bookId) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);

        if (optionalBook.isEmpty()) {
            throw new RuntimeException("❌ No book found with ID: " + bookId);
        }

        Book book = optionalBook.get();
        book.setName(bookRequestDto.getName());
        book.setGenre(bookRequestDto.getGenre());
        book.setPages(bookRequestDto.getPages());
        book.setPublisherName(bookRequestDto.getPublisherName());
        book.setCopies(bookRequestDto.getCopies());

        Author author = authorRepository.findById(bookRequestDto.getAuthorId())
                .orElseThrow(() -> new RuntimeException("❌ Author not found with ID: " + bookRequestDto.getAuthorId()));
        book.setAuthor(author);

        bookRepository.save(book);
        return "✅ Book updated successfully";
    }

    public ResponseEntity<BookAuthorAndStudentsDto> bookAuthorAndStudentsDtoResponseEntity(int bookId) {
        List<Transaction> transactionList=new ArrayList<>();
        Optional<Book> book =bookRepository.findById(bookId);
        if(book.isPresent()){
            String authorName=book.get().getAuthor().getName();
             transactionList=book.get().getTransactionsForBook();
            List<StudentResponseDto> studentListWhoBorrowedBook = new ArrayList<>();
            for(Transaction t:transactionList){
                // Collect only students who issued the book
                if(t.isIssueOperation()) {
                    Student student=t.getCard().getStudent();
                    StudentResponseDto dto= StudentConverter.convertToDto(student);
                    studentListWhoBorrowedBook.add(dto);

                }

            }
            BookAuthorAndStudentsDto dto =
                    new BookAuthorAndStudentsDto(authorName,studentListWhoBorrowedBook);
            return ResponseEntity.ok(dto);
        }
        return  ResponseEntity.notFound().build();
    }
}