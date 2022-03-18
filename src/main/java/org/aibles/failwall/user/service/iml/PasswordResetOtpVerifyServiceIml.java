package org.aibles.failwall.user.service.iml;

import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.aibles.failwall.exception.FailWallBusinessException;
import org.aibles.failwall.exception.FailWallSystemException;
import org.aibles.failwall.user.dto.request.PasswordResetOtpVerifyReqDto;
import org.aibles.failwall.user.dto.response.PasswordResetOtpVerifyResDto;
import org.aibles.failwall.user.repository.UserRepository;
import org.aibles.failwall.user.service.PasswordResetOtpVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class PasswordResetOtpVerifyServiceIml implements PasswordResetOtpVerifyService {

    private final UserRepository userRepository;
    private final LoadingCache<String, String> otpCache;
    private final LoadingCache<String, String> passwordResetKeyCache;

    @Autowired
    public PasswordResetOtpVerifyServiceIml(UserRepository userRepository,
                                            @Qualifier("otpCache") LoadingCache<String, String> otpCache,
                                            @Qualifier("passwordResetCache") LoadingCache<String, String> passwordResetKeyCache) {
        this.userRepository = userRepository;
        this.otpCache = otpCache;
        this.passwordResetKeyCache = passwordResetKeyCache;
    }

    @Override
    public PasswordResetOtpVerifyResDto execute(PasswordResetOtpVerifyReqDto passwordResetOtpVerifyReq) {
        validateInput(passwordResetOtpVerifyReq);

        //Generate passwordResetKey and put it in cache
        final String passwordResetKey = generatePasswordResetKey(passwordResetOtpVerifyReq.getEmail());
        passwordResetKeyCache.put(passwordResetOtpVerifyReq.getEmail().toUpperCase(), passwordResetKey);

        return new PasswordResetOtpVerifyResDto(passwordResetKey);
    }

    private void validateInput(PasswordResetOtpVerifyReqDto passwordResetOtpVerifyReq){
        Map<String, String> errorMap = new HashMap<>();

        userRepository.findByEmail(passwordResetOtpVerifyReq.getEmail())
                .ifPresentOrElse(
                        user -> {
                            try{
                                String otp = otpCache.get(passwordResetOtpVerifyReq.getEmail().toUpperCase());

                                if (otp.equals(passwordResetOtpVerifyReq.getEmail().toUpperCase())){
                                    errorMap.put("otp", "Otp is expired");
                                }
                                else if (!otp.equals(passwordResetOtpVerifyReq.getOtp())){
                                    errorMap.put("otp", "Invalid otp.");
                                }

                            } catch (ExecutionException e){
                                log.error("Fail to get value from guava cache");
                                throw new FailWallSystemException("Internal Server Error");
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

    private String generatePasswordResetKey(final String email){
        return Base64.getEncoder().encodeToString(email.getBytes());
    }
}
