package org.aibles.failwall.user.service.iml;

import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;
import org.aibles.failwall.exception.FailWallBusinessException;
import org.aibles.failwall.user.dto.request.UserRegisterActiveReqDto;
import org.aibles.failwall.user.repository.UserRepository;
import org.aibles.failwall.user.service.UserRegisterActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class UserRegisterActiveServiceIml implements UserRegisterActiveService {

    private final UserRepository userRepository;
    private final LoadingCache<String, String> otpCache;

    @Autowired
    public UserRegisterActiveServiceIml(UserRepository userRepository,
                                        LoadingCache<String, String> otpCache) {
        this.userRepository = userRepository;
        this.otpCache = otpCache;
    }

    @Override
    public void execute(UserRegisterActiveReqDto userRegisterActiveReq) {

        validateInput(userRegisterActiveReq);

        userRepository.findByEmail(userRegisterActiveReq.getEmail())
                .map(u -> {
                    u.setActivated(true);
                    return u;
                })
                .map(userRepository::save);
    }

    private void validateInput(UserRegisterActiveReqDto activeUserFormRequestDto) {
        HashMap<String, String> error = new HashMap<>();

        String email = activeUserFormRequestDto.getEmail();
        String otpCode = activeUserFormRequestDto.getOtp();

        //Validate user
        userRepository.findByEmail(email).ifPresentOrElse(
                user -> {
                    if (userRepository.isActiveUser(user.getEmail())) {
                        error.put("email", "Email is activated");
                    }
                },
                () -> error.put("email", "Email is not registered")
        );

        //Validate otp
        try {
            if (!otpCache.get(email).equals(otpCode)) {
                error.put("otp", "In-correct otp");
            }
            else if (otpCache.equals(email.toUpperCase())){
                error.put("otp", "Otp expired");
            }
        } catch (ExecutionException e) {
            log.error("Can not read otp from guava cache");
            throw new FailWallBusinessException("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!error.isEmpty()) {
            throw new FailWallBusinessException(error, HttpStatus.BAD_REQUEST);
        }
    }
}
