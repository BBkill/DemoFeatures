package org.aibles.failwall.user.service.iml;

import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.aibles.failwall.exception.FailWallBusinessException;
import org.aibles.failwall.exception.FailWallSystemException;
import org.aibles.failwall.user.dto.request.PasswordResetReqDTO;
import org.aibles.failwall.user.repository.UserRepository;
import org.aibles.failwall.user.service.PasswordResetUpdatePassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class PasswordResetUpdatePassServiceIml implements PasswordResetUpdatePassService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LoadingCache<String, Object> passwordResetCache;

    @Autowired
    public PasswordResetUpdatePassServiceIml(PasswordEncoder passwordEncoder,
                                             UserRepository userRepository,
                                             @Qualifier("passwordResetCache") LoadingCache<String, Object> passwordResetCache) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.passwordResetCache = passwordResetCache;
    }

    @Override
    public void execute(PasswordResetReqDTO passwordResetReq) {

        validateInput(passwordResetReq);

        userRepository.updatePassword(passwordResetReq.getEmail(),
                passwordEncoder.encode(passwordResetReq.getNewPassword()));
    }

    private void validateInput(PasswordResetReqDTO passwordResetReq){
        Map<String, String> errorMap = new HashMap<>();

        final String email = passwordResetReq.getEmail();
        final String newPassword = passwordResetReq.getNewPassword();
        final String passwordConfirm = passwordResetReq.getPasswordConfirm();
        final String passwordResetKey = passwordResetReq.getPasswordResetKey();

        //validate email
        if (!userRepository.findByEmail(email).isPresent()) {
            errorMap.put("email", "Email is not registered");
        }

        //validate password reset key
        try {
            String key = passwordResetCache.get(email.toUpperCase()).toString();

            if (key.equals(email.toUpperCase())) {
                errorMap.put("passwordResetKey", "PasswordResetKey expired");
            }
            else if (!key.equals(passwordResetKey.toUpperCase())) {
                errorMap.put("passwordResetKey", "Invalid passwordResetKey");
            }
        } catch (ExecutionException e) {
            log.error("Can not get passwordResetKey from guava cache");
            throw new FailWallSystemException("Internal server error");
        }

        //validate pass and passConfirm
        if (!newPassword.equals(passwordConfirm)) {
            errorMap.put("passwordConfirm", "PasswordConfirm not match with newPassword");
        }

        if (!errorMap.isEmpty()) {
            throw new FailWallBusinessException(errorMap, HttpStatus.BAD_REQUEST);
        }
    }
}
