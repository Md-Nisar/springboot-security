package com.mna.springbootsecurity.mail.service.impl;

import com.mna.springbootsecurity.base.vo.message.EmailNotificationData;
import com.mna.springbootsecurity.mail.helper.AuthLinkHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailPreparer {

    private final TemplateEngine templateEngine;
    private final AuthLinkHelper urlGenerator;

    public String prepareWelcomeEmail(EmailNotificationData emailNotification) {
        return String.format(
                        """
                                Dear %s, Congratulations!
                                Welcome to our service! We are excited to have you with us.
                                Best regards,
                                The Team,
                        """,
                emailNotification.getEmailAddress()
        );
    }

    public String prepareUserVerificationEmail(EmailNotificationData emailNotification) {
        Context context = new Context();
        context.setVariable("userName", emailNotification.getEmailAddress());

        String link = urlGenerator.generateUserVerificationLink(
                emailNotification.getEmailAddress(),
                emailNotification.getData().toString(),
                true
        );
        context.setVariable("verificationLink", link);

        return templateEngine.process("user-verification-email", context);
    }

    public String prepareResetPasswordEmail(EmailNotificationData emailNotification) {
        Context context = new Context();
        context.setVariable("userName", emailNotification.getEmailAddress());

        String link = urlGenerator.generateResetPasswordLink(
                emailNotification.getEmailAddress(),
                emailNotification.getData().toString(),
                true
        );
        context.setVariable("resetPasswordLink", link);

        return templateEngine.process("reset-password-email", context);
    }

    // Add more methods for different email purposes as needed
}

