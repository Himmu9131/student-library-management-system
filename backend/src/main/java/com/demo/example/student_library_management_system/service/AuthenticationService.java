package com.demo.example.student_library_management_system.service;

import com.demo.example.student_library_management_system.dto.AuthenticationRequest;
import com.demo.example.student_library_management_system.dto.RegisterRequestDto;
import com.demo.example.student_library_management_system.enums.Role;
import com.demo.example.student_library_management_system.model.Student;
import com.demo.example.student_library_management_system.model.User;
import com.demo.example.student_library_management_system.repository.StudentRepository;
import com.demo.example.student_library_management_system.repository.UserRepository;
import com.demo.example.student_library_management_system.response.AuthenticationResponse;
import com.demo.example.student_library_management_system.utils.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private JavaMailSender javaMailSender;

    public AuthenticationResponse register(RegisterRequestDto request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole().toUpperCase()))
                .build();

        User savedUser = userRepository.save(user);

        // 🔗 Auto-link User with Student (using email or username match)
        Optional<Student> optionalStudent = studentRepository.findByEmail(user.getUsername());
        optionalStudent.ifPresent(student -> {
            student.setUser(savedUser);
            studentRepository.save(student);
        });

        String jwtToken = jwtService.generateToken(savedUser);
        return new AuthenticationResponse(jwtToken, user.getRole().name());
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword())
        );

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken, user.getRole().name());
    }
}

