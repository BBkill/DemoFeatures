package org.aibles.failwall.user.service;

import org.aibles.failwall.user.dto.request.PasswordResetOtpVerifyReqDto;
import org.aibles.failwall.user.dto.response.PasswordResetOtpVerifyResDto;

public interface PasswordResetOtpVerifyService {

    PasswordResetOtpVerifyResDto execute(PasswordResetOtpVerifyReqDto passwordResetOtpVerifyReq);

}
