package com.demo.example.student_library_management_system.service;

import com.demo.example.student_library_management_system.controller.StudentController;
import com.demo.example.student_library_management_system.converters.StudentConverter;
import com.demo.example.student_library_management_system.dto.StudentRequestDto;
import com.demo.example.student_library_management_system.enums.CardStatus;
import com.demo.example.student_library_management_system.enums.Role;
import com.demo.example.student_library_management_system.model.Card;
import com.demo.example.student_library_management_system.model.Student;
import com.demo.example.student_library_management_system.model.User;
import com.demo.example.student_library_management_system.repository.StudentRepository;
import com.demo.example.student_library_management_system.repository.UserRepository;
import com.demo.example.student_library_management_system.response.StudentResponseDto;
import com.demo.example.student_library_management_system.utils.MailService;
import com.demo.example.student_library_management_system.utils.PasswordGenerator;
import com.demo.example.student_library_management_system.utils.StringFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private PasswordGenerator passwordGenerator;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private StringFormatter stringFormatter;
    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public String addStudent(StudentRequestDto studentRequestDto) {

        logger.info("addStudent method started");
        Student student = StudentConverter.convertStudentRequestDtoToStudent(studentRequestDto);

        // whenever the student is created card is also created for that student

        // Step 2: Create User using email as username
        User user = new User();
        user.setUsername(student.getEmail()); // email as username
        // generate random password
        String tempPass=PasswordGenerator.generateTempPassword();
        user.setPassword(passwordEncoder.encode(tempPass));
        user.setRole(Role.STUDENT);
        user.setStudent(student); // set student in user

        // Step 3: Set user in student
        student.setUser(user);

        User savedUser = userRepository.save(user);
        // Step 4: Create and attach card
        Card card = new Card();
        card.setCardStatus(CardStatus.ACTIVE);
        card.setStudent(student);
        student.setCard(card);

        //mail sending process Here---
        String studentEmail=student.getEmail();


        // Step 5: Save user and student (cascade should handle card)
        userRepository.save(user); // this saves user and student because of the relationship
        logger.info("Student along with card and user is saved in database");

        logger.info("addStudent method ended");

        try {
            notifyStudent(studentEmail, tempPass, student.getName());
        } catch (Exception e) {
            logger.error("Email sending failed: " + e.getMessage());
        }
        return "Student, User, and Card created successfully";


    }

    public List<StudentResponseDto> getAll() {
        List<Student> students = studentRepository.findAll();

        if (students.isEmpty()) {
            throw new RuntimeException("Students are not present");
        }

        // Convert List<Student> to List<StudentResponseDto>
        return students.stream()
                .map(StudentConverter::convertToDto)
                .collect(Collectors.toList());
    }

    /*
    Pagination - fetching records or data in the form of pages
    pagenumber - the number of page we want to see(page number starts from 0,1,2...)
    pagesize - total number of records in each page( and it is fixed in each page)
    database total records-28, pagesize -5
    0th page -1-5
    1st page-6-10
    2nd page-11-15
    3rd page-16-20
    4th page-21-25
    5th page-26-28

    database record-11, pagesize-2
    0th page -1-2
    1st- 3-4
    2nd - 5-6
    3rd-7-8
    4th-9-10
    5th-11
    sorting - arranging the data or records based on ascending or descending order
     */
    public List<Student> getAllStudentsByPage(int pageNo, int pageSize, String sortInput) {
        // only pagination - Page<Student> studentPage = studentRepository.findAll(PageRequest.of(pageNo,pageSize));
        //pagination + sorting
        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(pageNo, pageSize, Sort.by(sortInput).ascending()));
        // we cannot send studentPage output directly
        // so we convert the page into list and send list as output
        List<Student> studentList = new ArrayList<>();
        for (Student stu1 : studentPage) {
            studentList.add(stu1);
        }
        return studentList;

    }


    public Student getStudentById(int studentId) {
        logger.info("getStudentById method started");
        Student student = studentRepository.findById(studentId).get();
        if (student == null) {
            logger.error("student not found");
            throw new RuntimeException();
        }
        logger.info("getStudentById method ended");
        return student;
    }

    public String deleteStudentById(int studentId) {

        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("❌ Student with ID " + studentId + " does not exist.");
        }
        studentRepository.deleteById(studentId);

        return "Student with ID " + studentId + " was deleted.";

    }

    public String countStudent() {
        long totalCount = studentRepository.count();
        return "The total number of students present is : " + totalCount;
    }

    public String updateStudent(StudentRequestDto studentRequestDto, int studentId) {
        Student student = getStudentById(studentId);
        if (student == null) {
            throw new RuntimeException("No student present with id : " + studentId);
        }
        if (student != null) {
            student.setName(studentRequestDto.getName());
            student.setMobile(studentRequestDto.getMobile());
            student.setEmail(studentRequestDto.getEmail());
            student.setAge(studentRequestDto.getAge());
            student.setAddress(studentRequestDto.getAddress());
            studentRepository.save(student);
            return "student updated successfully";
        } else {
            return "no student found cannot update";
        }
    }


    public Student getStudentByEmailQuery(String email) {
        Student student = studentRepository.getStudentByEmail(email);
        return student;
    }


    public void notifyStudent(String studentEmail,String tempPassword,String name) {

        String subject = "Library Registration Successful.!";
        String body = "Dear "+name+",\n\nYou have Successfully registered in  Library Application." +
                "\n\nYour Temporary password is : "+tempPassword+
                "\n\nEnjoy the Services" + "\n\nRegards,\nLibrary Team";
        mailService.sendEmail(studentEmail,subject,body);
    }

}