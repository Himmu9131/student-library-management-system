package com.demo.example.student_library_management_system.service;

import com.demo.example.student_library_management_system.model.User;
import com.demo.example.student_library_management_system.repository.UserRepository;
import com.demo.example.student_library_management_system.utils.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
      @Autowired
      private JwtService jwtService;
      @Autowired
      private UserRepository userRepository;
    public Integer getStudentIdFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization").substring(7); // Remove "Bearer "
        String username = jwtService.extractUsername(token);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getStudent().getId();  // ✅ Only works if User ↔ Student is mapped
    }
}
