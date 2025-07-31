package com.demo.example.student_library_management_system.controller;

import com.demo.example.student_library_management_system.dto.AuthenticationRequest;
import com.demo.example.student_library_management_system.dto.ForgotPassDto;
import com.demo.example.student_library_management_system.dto.RegisterRequestDto;
import com.demo.example.student_library_management_system.dto.UpdatePasswordRequestDto;
import com.demo.example.student_library_management_system.model.User;
import com.demo.example.student_library_management_system.repository.UserRepository;
import com.demo.example.student_library_management_system.response.AuthenticationResponse;
import com.demo.example.student_library_management_system.service.AuthenticationService;
import com.demo.example.student_library_management_system.utils.MailService;
import com.demo.example.student_library_management_system.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthenticationService authenticationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequestDto request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPassDto request) {
        String email = request.getEmail().toLowerCase();
        if (email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }
        Optional<User> optionalUser = userRepository.findByUsername(email);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body("No user found with that email.");
        }

        User user = optionalUser.get();
        String tempPassword = PasswordGenerator.generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPassword));
        userRepository.save(user);

        mailService.sendForgotPasswordEmail(email, tempPassword);
        return ResponseEntity.ok("Temporary password sent.");
    }

    @PutMapping("/update-password")
    public HttpStatus updatePassword(@RequestBody UpdatePasswordRequestDto request){

        if (!request.getNewPass().equals(request.getConfirmPass())) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }

        userRepository.findByUsername(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String encodedNewPass = passwordEncoder.encode(request.getNewPass());
        userRepository.updatePassword(request.getEmail(), encodedNewPass);

        return HttpStatus.CREATED;
    }


}

