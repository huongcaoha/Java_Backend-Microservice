package org.example.pharmacykafka.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender; // Phải dùng JavaMailSender và KHÔNG static

    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        // Đoạn này giúp thầy kiểm tra nếu vẫn bị lỗi cấu hình
        if (mailSender == null) {
            throw new RuntimeException("MailSender chưa được khởi tạo! Kiểm tra thư viện và YML.");
        }

        mailSender.send(message);
    }
}