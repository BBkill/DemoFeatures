package org.aibles.failwall.user.service.iml;

import com.google.common.cache.LoadingCache;
import org.aibles.failwall.exception.FailWallBusinessException;
import org.aibles.failwall.mail.dto.MailRequestDto;
import org.aibles.failwall.mail.MailService;
import org.aibles.failwall.util.helper.OtpHelper;
import org.aibles.failwall.user.dto.request.RegisterReqDto;
import org.aibles.failwall.user.dto.response.RegisterResDto;
import org.aibles.failwall.user.model.User;
import org.aibles.failwall.user.repository.UserRepository;
import org.aibles.failwall.user.service.UserRegisterService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserRegisterServiceIml implements UserRegisterService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final LoadingCache<String, String> otpCache;
    private final ModelMapper modelMapper;
    private final MailService mailService;

    @Autowired
    public UserRegisterServiceIml(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder,
                                  LoadingCache<String, String> otpCache,
                                  ModelMapper modelMapper,
                                  MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.otpCache = otpCache;
        this.modelMapper = modelMapper;
        this.mailService = mailService;
    }

    @Override
    public RegisterResDto execute(RegisterReqDto registerReq) {
        //Validate input
        validateInput(registerReq);

        //Create user entity from request
        User user = modelMapper.map(registerReq, User.class);
        user.setPassword(passwordEncoder.encode(registerReq.getPassword()));
        user.setActivated(false);

        //Save user to database
        user = userRepository.save(user);

        //Generate otp for active user
        String activeUserOtp = OtpHelper.generateOTP();
        otpCache.put(user.getEmail().toUpperCase(), activeUserOtp);

        //send mail request user active account
        this.sendMail(user.getEmail(), activeUserOtp);

        return modelMapper.map(user, RegisterResDto.class);
    }

    private void validateInput(RegisterReqDto registerReq) {
        HashMap<String, String> errorMap = new HashMap<>();

        //Validate email with unique constraint
        userRepository.findByEmail(registerReq.getName())
                .ifPresent(user -> errorMap.put("user", "email is already existed"));

        //Validate password confirm
        if (!registerReq.getPassword().equals(registerReq.getPasswordConfirm())){
            errorMap.put("passwordConfirm", "PasswordConfirm not match to password");
        }

        if (!errorMap.isEmpty()) {
            throw new FailWallBusinessException(errorMap, HttpStatus.BAD_REQUEST);
        }
    }

    void sendMail(final String email, String otpCode) {
        String message = "Your confirm register account OTP code is: " +
                otpCode +
                ". This OTP code will be expired about 3 minutes.";

        //Create mail message
        MailRequestDto mailRequestDTO = new MailRequestDto();
        mailRequestDTO.setReceiver(email);
        mailRequestDTO.setMessage(message);
        mailRequestDTO.setSubject("verify account");

        mailService.sendMail(mailRequestDTO);
    }
}
