package org.aibles.failwall.user.service.iml;

import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.aibles.failwall.exception.FailWallBusinessException;
import org.aibles.failwall.mail.dto.MailRequestDto;
import org.aibles.failwall.mail.iml.MailServiceIml;
import org.aibles.failwall.user.dto.request.PasswordResetOtpGetReqDto;
import org.aibles.failwall.user.repository.UserRepository;
import org.aibles.failwall.user.service.PasswordResetOtpGetService;
import org.aibles.failwall.util.helper.OtpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PasswordResetOtpGetServiceIml implements PasswordResetOtpGetService {

    private final UserRepository userRepository;
    private final MailServiceIml mailService;
    private final LoadingCache<String, String> otpCache;

    @Autowired
    public PasswordResetOtpGetServiceIml(UserRepository userRepository,
                                         MailServiceIml mailService,
                                         LoadingCache<String, String> otpCache) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.otpCache = otpCache;
    }

    @Override
    public void execute(PasswordResetOtpGetReqDto passwordResetOtpGetReq) {
        //Validate input
        validateInput(passwordResetOtpGetReq);

        //Generate otp and put it to otpCache
        String otp = OtpHelper.generateOTP();
        otpCache.put(passwordResetOtpGetReq.getEmail().toUpperCase(), otp);

        //Send otp to user's mail
        sendMailOtpResetPass(passwordResetOtpGetReq.getEmail(), otp);
    }

    public void validateInput(PasswordResetOtpGetReqDto passwordResetOtpGetReq){
        userRepository.findByEmail(passwordResetOtpGetReq.getEmail())
                .orElseThrow(() -> {
                    throw new FailWallBusinessException(
                            "Email is not registered", HttpStatus.BAD_REQUEST
                    );
                });
    }

    public void sendMailOtpResetPass(final String email, final String otp){
        final String message = new StringBuilder()
                .append("Your confirm reset password OTP code is ")
                .append(otp)
                .append(". This OTP will be expired about 3 minutes.").toString();

        MailRequestDto mailRequestDTO = new MailRequestDto();
        mailRequestDTO.setReceiver(email);
        mailRequestDTO.setSubject("Confirm reset password");
        mailRequestDTO.setMessage(message);

        mailService.sendMail(mailRequestDTO);
    }
}
