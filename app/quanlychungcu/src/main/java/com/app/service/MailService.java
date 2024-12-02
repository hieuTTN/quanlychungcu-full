package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MailService {

    public void sendAsyncEmail(String to, String subject, String text){
        ExecutorService es = Executors.newCachedThreadPool();
        es.execute(new Runnable() {
            @Override
            public void run() {
                sendEmail(to, subject, text);
            }
        });
        es.shutdown();
    }

    private void sendEmail(String to, String subject, String text){
        // Cấu hình thông tin máy chủ gửi email
        String host = "smtp.gmail.com"; // Địa chỉ SMTP
        String from = "dev002102@gmail.com"; // Email của bạn
        String password = "vlpenjdvmhzajkjc"; // Mật khẩu email

        // Cấu hình các thuộc tính cần thiết
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");

        // Tạo một phiên làm việc (session) với các thuộc tính cấu hình
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });
        // Tạo một đối tượng MimeMessage (email)
        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
            message.setSubject(subject);
            message.setContent(text, "text/html");
            // Gửi email
            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Email đã được gửi thành công.");
    }

}

