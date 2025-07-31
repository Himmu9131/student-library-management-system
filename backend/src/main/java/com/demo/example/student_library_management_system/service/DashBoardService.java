package com.demo.example.student_library_management_system.service;

import com.demo.example.student_library_management_system.dto.BorrowedBookDto;
import com.demo.example.student_library_management_system.dto.RecentActivityDto;
import com.demo.example.student_library_management_system.dto.StudentDashboardDto;
import com.demo.example.student_library_management_system.model.Student;
import com.demo.example.student_library_management_system.model.User;
import com.demo.example.student_library_management_system.repository.StudentRepository;
import com.demo.example.student_library_management_system.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashBoardService {
    @Autowired
    private final BookService bookService;
    @Autowired
    private final TransactionService transactionService;
    @Autowired
    private final StudentService studentService;
    @Autowired
    private final StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;

    // Find Student by username
    public StudentDashboardDto getStudentDashboard(String username) {
        // Fetch user and student
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Student student = studentRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        int booksBorrowed = transactionService.getBorrowedBooksByStudentId(student.getId()).size();
        int overdueBooks = transactionService.countOverdueByStudentId(student.getId());
        double totalFine = transactionService.getTotalFineByStudentId(student.getId());

        // Fetch list-type values
        List<BorrowedBookDto> currentBooks = transactionService.getBorrowedBooksByStudentId(student.getId());

        // Set all fields
        StudentDashboardDto dto = new StudentDashboardDto();
        dto.setBooksBorrowed(booksBorrowed);
        dto.setTotalFine(totalFine);
        dto.setOverdueBooks(overdueBooks);
        dto.setWelcomeMessage("Welcome, " + student.getName() + "!");
        dto.setStudentName(student.getName());
        dto.setCurrentBorrowedBooks(currentBooks);
        return dto;
    }

}
