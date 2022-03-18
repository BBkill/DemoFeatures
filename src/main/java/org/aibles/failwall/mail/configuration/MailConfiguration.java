package org.aibles.failwall.mail.configuration;

import static org.aibles.failwall.mail.constant.MailConfigProperties.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfiguration {

    @Value("${spring.mail.username}")
    private String mailUsername;

    @Value("${spring.mail.password}")
    private String mailPassword;

    @Bean
    public JavaMailSender createJavaMailSenderBean(){
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(MAIL_HOST);
        javaMailSender.setPort(MAIL_PORT);

        System.out.println("mail " + mailPassword);

        javaMailSender.setUsername(mailUsername);
        javaMailSender.setPassword(mailPassword);

        Properties props = javaMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", MAIL_TRANSPORT_PROTOCOL);
        props.put("mail.smtp.auth", MAIL_SMTP_AUTH);
        props.put("mail.smtp.starttls.enable", MAIL_SMTP_START_TLS);

        return javaMailSender;
    }

}
