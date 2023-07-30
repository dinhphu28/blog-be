package tech.dinhphu28.blog.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMailSMTPService {

    private final JavaMailSender mailSender;

    public void sendHtmlEmail(String subject, String htmlContent, String receiver) throws MessagingException {

        MimeMessage message = mailSender.createMimeMessage();

        boolean multipart = true;

        MimeMessageHelper helper = new MimeMessageHelper(message, multipart, "utf-8");

        message.setContent(htmlContent, "text/html; charset=UTF-8");

        helper.setTo(receiver);
        helper.setSubject(subject);

        mailSender.send(message);
    }
}
