package tech.dinhphu28.blog.service;

import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import tech.dinhphu28.blog.config.MailConfig;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class SendMailSMTPServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private SendMailSMTPService underTest;

    @BeforeEach
    void setUp() {
        underTest = new SendMailSMTPService(mailSender);
    }

    @Test
    void sendHtmlEmail() throws MessagingException, IOException {
        // given
        String subject = "Hello";
        String htmlContent = "This is content";
        String receiver = "jack@mail.com";
        given(mailSender.createMimeMessage()).willReturn(new MimeMessage((Session) null));

        // when
        underTest.sendHtmlEmail(subject, htmlContent, receiver);

        // then
        ArgumentCaptor<MimeMessage> mimeMessageArgumentCaptor = ArgumentCaptor.forClass(MimeMessage.class);
        verify(mailSender).send(mimeMessageArgumentCaptor.capture());
        MimeMessage message = mimeMessageArgumentCaptor.getValue();

        assertThat(message.getContent()).isEqualTo(htmlContent);
    }
}