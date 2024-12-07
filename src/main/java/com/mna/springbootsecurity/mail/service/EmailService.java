package com.mna.springbootsecurity.mail.service;

import jakarta.mail.MessagingException;

import java.io.File;

public interface EmailService {

    void sendSimpleEmail(String to, String subject, String text);

    void sendHtmlEmail(String to, String subject, String htmlContent) throws MessagingException;

    void sendEmailWithAttachment(String to, String subject, String text, File attachment) throws MessagingException;
}

