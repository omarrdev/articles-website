package com.omarrdev.ithra.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    @Async
    public void sendVerificationEmail(String toEmail, String username, String token) {
        Context ctx = new Context();
        ctx.setVariable("username", username);
        ctx.setVariable("verificationLink", frontendUrl + "/verify-email?token=" + token);
        sendEmail(toEmail, "Verify your email — Ithra", "email/verify-email", ctx);
    }

    @Async
    public void sendForgotPasswordEmail(String toEmail, String username, String token) {
        Context ctx = new Context();
        ctx.setVariable("username", username);
        ctx.setVariable("resetLink", frontendUrl + "/reset-password?token=" + token);
        sendEmail(toEmail, "Reset your password — Ithra", "email/forgot-password", ctx);
    }

    @Async
    public void sendPasswordChangedEmail(String toEmail, String username) {
        Context ctx = new Context();
        ctx.setVariable("username", username);
        sendEmail(toEmail, "Your password has been changed — Ithra", "email/password-changed", ctx);
    }

    private void sendEmail(String to, String subject, String templateName, Context ctx) {
        try {
            String html = templateEngine.process(templateName, ctx);
            mailSender.send(message -> {
                MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
                helper.setFrom(fromEmail);
                helper.setTo(to);
                helper.setSubject(subject);
                helper.setText(html, true);
            });
        } catch (Exception e) {
            // Log email failure without crashing the main flow
        }
    }
}
