package org.aibles.failwall.mail;

import org.aibles.failwall.mail.dto.MailRequestDto;

public interface MailService {
    void sendMail (MailRequestDto mailRequestDTO);
}
