package com.scm.services.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.scm.services.EmailService;

import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender eMailSender;

    @Value("${spring.mail.properties.domain_name}")
    private String domainName;

    @Override
    public void sendEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(domainName);
        eMailSender.send(message);

    }

    @Override
    public void sendEmailWithHtml() {
        try {
            MimeMessage message = eMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            
            helper.setTo("sunny8jankumar@gmail.com");
            helper.setSubject("Welcome to SCM!");
            helper.setText("<h1>Hello from SCM</h1><p>This is an <b>HTML email</b>.</p>", true);
            helper.setFrom(domainName);
            eMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
          }
    }


    @Override
    public void sendEmailWithAttachment() {
        try {
            MimeMessage message = eMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
    
            helper.setTo("recipient@example.com");
            helper.setSubject("Document Attached");
            helper.setText("Hi, please find the attached document.", false);
            helper.setFrom(domainName);
    
            FileSystemResource file = new FileSystemResource(new File("report.pdf"));
            helper.addAttachment("document.pdf", file);
    
            eMailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
