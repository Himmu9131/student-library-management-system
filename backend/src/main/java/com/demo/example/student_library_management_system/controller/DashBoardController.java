package com.demo.example.student_library_management_system.controller;

import com.demo.example.student_library_management_system.dto.StudentDashboardDto;
import com.demo.example.student_library_management_system.service.DashBoardService;
import com.demo.example.student_library_management_system.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashBoardController {

    private StudentDashboardDto studentDashboardDto;
    private final DashBoardService dashboardService;
    private final JwtService jwtService;
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @GetMapping("/student")
    public ResponseEntity<StudentDashboardDto> getStudentDashboard(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Remove "Bearer "
        String username = jwtService.extractUsername(token);
        StudentDashboardDto dto = dashboardService.getStudentDashboard(username);
        return ResponseEntity.ok(dto);
    }
}
