package com.cg.siptracker.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("praveensharma20113@gmail.com");
        simpleMailMessage.setTo(toEmail);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);
        mailSender.send(simpleMailMessage);
    }

    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("praveensharma20113@gmail.com");
        mailMessage.setTo(to);
        mailMessage.setSubject("Your OTP for Password Reset!.");
        mailMessage.setText("Your OTP for resetting your password is: " + otp + "\n\nThis OTP will expire in 5 minutes.");
        mailSender.send(mailMessage);
        System.out.println("OTP email sent to : " + to);
    }

    public void sendCsvWithAttachment(String to, String subject, String body, byte[] csvBytes, String filename) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);
            helper.addAttachment(filename, new ByteArrayResource(csvBytes));
            mailSender.send(message);
            log.info("CSV report emailed to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send CSV email to {}: {}", to, e.getMessage());
        }
    }



}