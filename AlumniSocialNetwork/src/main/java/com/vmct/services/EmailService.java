package com.vmct.services;

import jakarta.mail.MessagingException;
import java.util.List;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendEmailToGroup(List<String> recipients, String subject, String body);
}
