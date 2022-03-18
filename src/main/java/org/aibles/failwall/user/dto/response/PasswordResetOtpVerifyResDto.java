package org.aibles.failwall.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordResetOtpVerifyResDto {
    private String passwordResetKey;
}
