package com.vmct.configs;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Resource
    private Environment env;

    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        String portStr = env.getProperty("mail.port");
        if (portStr == null)
            throw new RuntimeException("Missing mail.port in mail.properties");

        mailSender.setHost(env.getProperty("mail.host"));
        mailSender.setPort(Integer.parseInt(portStr));
        mailSender.setUsername(env.getProperty("mail.username"));
        mailSender.setPassword(env.getProperty("mail.password"));

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.smtp.auth", env.getProperty("mail.smtp.auth", "true"));
        props.put("mail.smtp.starttls.enable", env.getProperty("mail.smtp.starttls.enable", "true"));
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
