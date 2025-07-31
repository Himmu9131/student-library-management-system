package com.demo.example.student_library_management_system.converters;

import com.demo.example.student_library_management_system.dto.BookRequestDto;
import com.demo.example.student_library_management_system.model.Book;



public class BookConverter {

    public static Book convertBookRequestDtoToBook(BookRequestDto bookRequestDto){
        boolean isAvailable = bookRequestDto.getCopies() > 0;
        Book book = Book.builder().name(bookRequestDto.getName()).genre(bookRequestDto.getGenre())
                .pages(bookRequestDto.getPages())
                .publisherName(bookRequestDto.getPublisherName()).copies(bookRequestDto.getCopies())
                .isAvailable(bookRequestDto.isAvailable())
                .build();

//        Book book = new Book();
//        book.setName(bookRequestDto.getName());
//        book.setGenre(bookRequestDto.getGenre());
//        book.setPages(bookRequestDto.getPages());
//        book.setPublisherName(bookRequestDto.getPublisherName());
//        book.setIssuedToStudent(bookRequestDto.isIssuedToStudent());
        return book;
    }
}
