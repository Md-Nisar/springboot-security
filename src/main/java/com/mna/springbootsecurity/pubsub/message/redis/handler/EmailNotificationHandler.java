package com.mna.springbootsecurity.pubsub.message.redis.handler;

import com.mna.springbootsecurity.base.vo.message.EmailNotificationData;
import com.mna.springbootsecurity.mail.enums.EmailSubject;
import com.mna.springbootsecurity.mail.service.EmailService;
import com.mna.springbootsecurity.mail.service.impl.EmailPreparer;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailNotificationHandler {

    private final EmailService emailService;
    private final EmailPreparer emailPreparer;

    public void handleEmailNotification(EmailNotificationData emailNotification) {
        switch (emailNotification.getType()) {
            case WELCOME:
                try {
                    sendWelcomeMail(emailNotification);
                } catch (MessagingException e) {
                    log.error("Unable to send 'Welcome email'", e);
                }
                break;

            case USER_VERIFICATION:
                try {
                    sendUserVerificationMail(emailNotification);
                } catch (MessagingException e) {
                    log.error("Unable to send 'User Verification email'", e);
                }
                break;

            case RESET_PASSWORD:
                try {
                    sendResetPasswordMail(emailNotification);
                } catch (MessagingException e) {
                    log.error("Unable to send 'Reset Password email'", e);
                }
                break;

                // Handle more email notifications as needed

            default:
                log.warn("Invalid Email Notification Type");
        }
    }

    private void sendWelcomeMail(EmailNotificationData emailNotification) throws MessagingException {
        emailService.sendHtmlEmail(
                emailNotification.getEmailAddress(),
                EmailSubject.WELCOME.getValue(),
                emailPreparer.prepareWelcomeEmail(emailNotification)
        );
    }

    private void sendUserVerificationMail(EmailNotificationData emailNotification) throws MessagingException {
        emailService.sendHtmlEmail(
                emailNotification.getEmailAddress(),
                EmailSubject.USER_VERIFICATION_LINK.getValue(),
                emailPreparer.prepareUserVerificationEmail(emailNotification)
        );
    }

    private void sendResetPasswordMail(EmailNotificationData emailNotification) throws MessagingException {
        emailService.sendHtmlEmail(
                emailNotification.getEmailAddress(),
                EmailSubject.RESET_PASSWORD_LINK.getValue(),
                emailPreparer.prepareResetPasswordEmail(emailNotification)
        );
    }

    // Add more methods to send different email as needed

}
