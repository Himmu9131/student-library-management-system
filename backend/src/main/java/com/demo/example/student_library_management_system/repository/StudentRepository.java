package com.demo.example.student_library_management_system.repository;

import com.demo.example.student_library_management_system.model.Student;
import com.demo.example.student_library_management_system.model.User;
import com.demo.example.student_library_management_system.response.StudentResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/*
class-> class->extedns
interface -> class-> implements
interafce -> interface -> extends
 */

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    // customized queries

    //1.writing own methods with fields or attributes with JPA support



    public Student findByEmailAndName(String email, String name);

    public List<Student> findByName(String name);

    public List<Student> findByAge(int age);

    public List<Student> findAll();

    //2.writing our own complete query without JPA support

    @Query(nativeQuery = true, value = "SELECT * FROM student where email=:inputEmail")
    public Student getStudentByEmail(String inputEmail);

    Optional<Student> findByUser(User user);

    Optional<Student> findByEmail(String email);


}
