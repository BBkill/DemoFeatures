package org.aibles.failwall.user.dto.request;

import lombok.Builder;
import lombok.Data;
import org.aibles.failwall.validation.opt.OtpConstraint;

import javax.validation.constraints.NotBlank;

@Builder
@Data
public class PasswordResetOtpVerifyReqDto extends PasswordResetOtpGetReqDto {

    @NotBlank(message = "OTP is required")
    @OtpConstraint
    private String otp;
}
