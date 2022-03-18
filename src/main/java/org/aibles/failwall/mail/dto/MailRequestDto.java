package org.aibles.failwall.mail.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MailRequestDto {
    private String receiver;
    private String subject;
    private String message;
}
