package com.mna.springbootsecurity.mail.service.impl;

import com.mna.springbootsecurity.logging.logger.EmailLogger;
import com.mna.springbootsecurity.mail.config.EmailConfiguration;
import com.mna.springbootsecurity.mail.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final EmailConfiguration emailConfig;

    public void sendSimpleEmail(String to, String subject, String text) {
        if (emailConfig.isEmailServiceEnabled()) {
            var message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            EmailLogger.logSendEmail(to, subject);
            try {
                javaMailSender.send(message);
                EmailLogger.logEmailSent();
            } catch (Exception ex) {
                EmailLogger.logError("Send simple email", ex);
            }
        } else {
            EmailLogger.logEmailServiceDisabled();
        }
    }

    public void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException {
        if (emailConfig.isEmailServiceEnabled()) {
            var message = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            EmailLogger.logSendEmail(to, subject);
            try {
                javaMailSender.send(message);
                EmailLogger.logEmailSent();
            } catch (Exception ex) {
                EmailLogger.logError("Send HTML email", ex);
            }
        } else {
            EmailLogger.logEmailServiceDisabled();
        }
    }

    public void sendEmailWithAttachment(String to, String subject, String text, File attachment) throws MessagingException {
        if (emailConfig.isEmailServiceEnabled()) {
            var message = javaMailSender.createMimeMessage();
            var helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            if (attachment != null && attachment.exists()) {
                helper.addAttachment(attachment.getName(), attachment);
            }

            EmailLogger.logSendEmail(to, subject);
            try {
                javaMailSender.send(message);
                EmailLogger.logEmailSent();
            } catch (Exception ex) {
                EmailLogger.logError("Send email with attachment", ex);
            }
        } else {
            EmailLogger.logEmailServiceDisabled();
        }
    }
}
