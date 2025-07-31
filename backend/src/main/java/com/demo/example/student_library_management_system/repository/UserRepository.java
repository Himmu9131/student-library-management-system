package com.demo.example.student_library_management_system.repository;

import com.demo.example.student_library_management_system.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User,Integer> {
   Optional<User> findByUsername(String username);

   @Modifying
   @Transactional
   @Query(value = "update user set password=:encodedNewPass where username=:email",nativeQuery = true)
   void updatePassword(String email, String encodedNewPass);


}
