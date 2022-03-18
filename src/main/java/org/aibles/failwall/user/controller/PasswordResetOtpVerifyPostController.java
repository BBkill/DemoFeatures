package org.aibles.failwall.user.controller;

import org.aibles.failwall.user.dto.request.PasswordResetOtpVerifyReqDto;
import org.aibles.failwall.user.dto.response.PasswordResetOtpVerifyResDto;
import org.aibles.failwall.user.service.PasswordResetOtpVerifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/forgot-password/otp")
public class PasswordResetOtpVerifyPostController {

    private final PasswordResetOtpVerifyService service;

    @Autowired
    public PasswordResetOtpVerifyPostController(PasswordResetOtpVerifyService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public PasswordResetOtpVerifyResDto execute(@RequestBody @Valid PasswordResetOtpVerifyReqDto
                                                           passwordResetOtpVerifyReq){
       return service.execute(passwordResetOtpVerifyReq);
    }

}
