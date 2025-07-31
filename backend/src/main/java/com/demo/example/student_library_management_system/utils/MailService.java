package com.demo.example.student_library_management_system.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail,String subject,String messageBody){

        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("96hdjohn@gmail.com");
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(messageBody);
        javaMailSender.send(message);

    }

    public void sendForgotPasswordEmail(String email, String tempPassword) {

        SimpleMailMessage message=new SimpleMailMessage();
        message.setFrom("96hdjohn@gmail.com");
        message.setTo(email);
        message.setSubject("📬 Your Temporary Password - Library System");
        message.setText("Dear User,\n\n"
                + "You requested a password reset. Here is your temporary password:\n\n"
                + tempPassword + "\n\n"
                + "Please log in using this password and change it immediately.\n\n"
                + "Regards,\n Library Management Team");

        javaMailSender.send(message);
    }
}
