package com.vmct.services.impl;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;
import com.vmct.services.EmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Async
    @Override
    public void sendEmail(String to, String subject, String content) {
        Email fromEmail = new Email("nhatlovely2017@gmail.com");
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/html", content);
        Mail mail = new Mail(fromEmail, subject, toEmail, emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request); // Lấy phản hồi từ SendGrid
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("✅ Email sent successfully to " + to + " at " + new java.util.Date());
            } else {
                System.err.println("❌ SendGrid error: Status " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (IOException ex) {
            System.err.println("❌ SendGrid error: " + ex.getMessage() + " at " + new java.util.Date());
            ex.printStackTrace();
        }
    }

    @Async
    @Override
    public void sendEmailToGroup(List<String> recipientEmails, String subject, String htmlContent) {
        if (recipientEmails == null || recipientEmails.isEmpty()) {
            System.out.println("⚠️ No recipients provided at " + new java.util.Date());
            return;
        }

        Email fromEmail = new Email("nhatlovely2017@gmail.com");
        Content emailContent = new Content("text/html", htmlContent);
        Mail mail = new Mail();
        mail.setFrom(fromEmail);
        mail.setSubject(subject);
        mail.addContent(emailContent);

        for (String recipient : recipientEmails) {
            if (recipient != null && !recipient.trim().isEmpty()) {
                Personalization personalization = new Personalization();
                personalization.addTo(new Email(recipient));
                mail.addPersonalization(personalization);
            }
        }

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request); // Lấy phản hồi từ SendGrid
            if (response.getStatusCode() >= 200 && response.getStatusCode() < 300) {
                System.out.println("✅ Email sent successfully to group at " + new java.util.Date());
            } else {
                System.err.println("❌ SendGrid error: Status " + response.getStatusCode() + " - " + response.getBody());
            }
        } catch (IOException ex) {
            System.err.println("❌ SendGrid error: " + ex.getMessage() + " at " + new java.util.Date());
            ex.printStackTrace();
        }
    }
}