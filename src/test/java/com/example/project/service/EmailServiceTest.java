package com.example.project.service;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private MimeMessage mimeMessage;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender);
        ReflectionTestUtils.setField(emailService, "mailFrom", "sender@example.com");
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
    }

    @Test
    void sendHtmlEmailSendsMimeMessage() {
        assertDoesNotThrow(() -> emailService.sendHtmlEmail(
                new String[]{"recipient@example.com"},
                new String[]{"cc@example.com"},
                new String[]{"bcc@example.com"},
                "Subject",
                "<p>Body</p>"
        ));

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }
}
