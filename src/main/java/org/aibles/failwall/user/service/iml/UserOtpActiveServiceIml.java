package org.aibles.failwall.user.service.iml;

import com.google.common.cache.LoadingCache;
import org.aibles.failwall.exception.FailWallBusinessException;
import org.aibles.failwall.mail.dto.MailRequestDto;
import org.aibles.failwall.mail.MailService;
import org.aibles.failwall.user.dto.request.UserOtpActiveReqDto;
import org.aibles.failwall.user.repository.UserRepository;
import org.aibles.failwall.util.helper.OtpHelper;
import org.aibles.failwall.user.service.UserOtpActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class UserOtpActiveServiceIml implements UserOtpActiveService {

    private final LoadingCache<String, String> otpCache;
    private final MailService mailService;
    private final UserRepository userRepository;

    @Autowired
    public UserOtpActiveServiceIml(LoadingCache<String, String> otpCache,
                                   MailService mailService, UserRepository userRepository) {
        this.otpCache = otpCache;
        this.mailService = mailService;
        this.userRepository = userRepository;
    }

    @Override
    public void execute(UserOtpActiveReqDto userOtpActiveReq) {

        validateInput(userOtpActiveReq);

        String otp = OtpHelper.generateOTP();
        otpCache.put(userOtpActiveReq.getEmail().toUpperCase(), otp);

        sendMail(userOtpActiveReq.getEmail(), otp);
    }

    private void validateInput(UserOtpActiveReqDto userActiveReq) {
        Map<String, Object> errorMap = new HashMap<>();

        userRepository.findByEmail(userActiveReq.getEmail())
                .ifPresentOrElse(
                        user -> {
                            if (user.isActivated()) {
                                errorMap.put("email", "Email is activated");
                            }
                        },
                        () -> {
                            errorMap.put("email", "Email is not registered");
                        }
                );

        if (!errorMap.isEmpty()) {
            throw new FailWallBusinessException(errorMap, HttpStatus.BAD_REQUEST);
        }
    }

    void sendMail(final String email, final String otp) {
        String message = new StringBuilder()
                .append("Your confirm register account OTP code is: ")
                .append(otp)
                .append(". This OTP code will be expired about 3 minutes.")
                .toString();

        MailRequestDto mailRequestDTO = new MailRequestDto();
        mailRequestDTO.setReceiver(email);
        mailRequestDTO.setMessage(message);
        mailRequestDTO.setSubject("Active Fail Wall account");

        mailService.sendMail(mailRequestDTO);
    }
}
