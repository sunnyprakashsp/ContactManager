package com.scm.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.scm.forms.ContactUsForm;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Controller
public class ContactUsController {

    @Autowired
    private JavaMailSender mailSender;

    @PostMapping("/contact")
    public String handleContactUs(@ModelAttribute ContactUsForm form) {
        
        String content = String.format(
            "New contact message:\n\nName: %s %s\nEmail: %s\nSubject: %s\n\nMessage:\n%s",
            form.getFirstName(), form.getLastName(),
            form.getEmail(), form.getContactUs(),  // Use the updated property
            form.getMessage()
        );

        // Create a MimeMessage
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "utf-8");
            helper.setTo("sunny8jankr@gmail.com");
            helper.setSubject("New Contact Form Submission: " + form.getContactUs());
            helper.setText(content, true);
        
            // Safe way to set reply-to
            String replyEmail = form.getEmail();
            if (replyEmail != null && !replyEmail.isBlank()) {
                message.setReplyTo(InternetAddress.parse(replyEmail));
            }
            
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        
        

        return "redirect:/thank-you";  // Redirect to a thank-you page after successful form submission
    }
}
