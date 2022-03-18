package org.aibles.failwall.user.service.iml;

import org.aibles.failwall.authentication.provider.JwtProvider;
import org.aibles.failwall.exception.FailWallBusinessException;
import org.aibles.failwall.user.dto.request.LoginReqDto;
import org.aibles.failwall.user.dto.response.LoginResDto;
import org.aibles.failwall.user.model.User;
import org.aibles.failwall.user.repository.UserRepository;
import org.aibles.failwall.user.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserLoginServiceIml implements UserLoginService {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserLoginServiceIml(final JwtProvider jwtProvider,
                               final UserRepository userRepository,
                               final PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public LoginResDto execute(LoginReqDto loginReq) {
        validateInput(loginReq);

        User user = userRepository.findByEmail(loginReq.getEmail()).get();

        //If user is activated, send token to response
        //If user is not activated, not sent token to response, but user still login success.
        if (user.isActivated()) {
            return LoginResDto.builder()
                    .accessToken(jwtProvider.generateToken(user.getEmail()))
                    .isActivated(user.isActivated())
                    .build();
        } else {
            return LoginResDto.builder()
                    .isActivated(user.isActivated())
                    .build();
        }
    }

    private void validateInput(LoginReqDto loginReq){
        Map<String, String> errorMap = new HashMap<>();

        userRepository.findByEmail(loginReq.getEmail())
                .ifPresentOrElse(
                        user -> {
                            if(!passwordEncoder.matches(loginReq.getPassword(), user.getPassword())){
                                errorMap.put("password", "Invalid password");
                            }
                        },
                        () -> errorMap.put("Email", "email is not registered")
                );

        if(!errorMap.isEmpty()){
            throw new FailWallBusinessException(errorMap, HttpStatus.UNAUTHORIZED);
        }
    }
}
