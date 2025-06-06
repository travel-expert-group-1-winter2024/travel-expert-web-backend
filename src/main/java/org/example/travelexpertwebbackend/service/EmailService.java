package org.example.travelexpertwebbackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("cluelessgamers7400@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true); // true enables HTML content
        System.out.println("Sending email...");
        mailSender.send(message);
    }

    public void sendBookingConfirmation(String to, String subject, String body, String pdfBase64)
            throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("cluelessgamers7400@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);

        // Convert Base64 string to byte array
        byte[] pdfBytes = Base64.getDecoder().decode(pdfBase64);

        // Attach PDF
        helper.addAttachment("BookingConfirmation.pdf",
                () -> new ByteArrayInputStream(pdfBytes),
                "application/pdf");

        mailSender.send(message);
    }
}

