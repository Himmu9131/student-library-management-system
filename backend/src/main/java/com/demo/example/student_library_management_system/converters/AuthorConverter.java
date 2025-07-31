package com.demo.example.student_library_management_system.converters;

import com.demo.example.student_library_management_system.dto.AuthorRequestDto;
import com.demo.example.student_library_management_system.model.Author;

public class AuthorConverter {

    public static Author convertAuthorRequestDtoToAuthor(AuthorRequestDto authorRequestDto){

        Author author = Author.builder().name(authorRequestDto.getName()).email(authorRequestDto.getEmail())
                .country(authorRequestDto.getCountry()).rating(authorRequestDto.getRating()).build();
        return author;
    }
}
