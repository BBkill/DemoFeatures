package org.aibles.failwall.mail.iml;

import org.aibles.failwall.mail.dto.MailRequestDto;
import org.aibles.failwall.mail.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MailServiceIml implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Autowired
    public MailServiceIml(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void sendMail(MailRequestDto mailRequestDTO) {
        javaMailSender.send(createMailMessage(mailRequestDTO));
    }

    private SimpleMailMessage createMailMessage(MailRequestDto mailRequestDTO){
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(mailFrom);
        mailMessage.setTo(mailRequestDTO.getReceiver());
        mailMessage.setSubject(mailRequestDTO.getSubject());
        mailMessage.setText(mailRequestDTO.getMessage());
        return mailMessage;
    }

}
