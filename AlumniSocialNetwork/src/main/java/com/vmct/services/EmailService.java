package com.vmct.services;

import jakarta.mail.MessagingException;
import java.util.List;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendEmailToGroup(List<String> recipientEmails, String subject, String htmlContent);
}