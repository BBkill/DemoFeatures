package org.aibles.failwall.user.controller;

import org.aibles.failwall.user.dto.request.PasswordResetOtpGetReqDto;
import org.aibles.failwall.user.service.PasswordResetOtpGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/forgot-password")
public class PasswordResetOtpPostController {

    private final PasswordResetOtpGetService service;

    @Autowired
    public PasswordResetOtpPostController(PasswordResetOtpGetService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void execute(@RequestBody @Valid PasswordResetOtpGetReqDto passwordResetOtpGetReq){
        service.execute(passwordResetOtpGetReq);
    }

}
