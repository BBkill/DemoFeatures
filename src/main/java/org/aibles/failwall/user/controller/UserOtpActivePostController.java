package org.aibles.failwall.user.controller;


import org.aibles.failwall.user.dto.request.UserOtpActiveReqDto;
import org.aibles.failwall.user.service.UserOtpActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/register/active")
public class UserOtpActivePostController {

    private final UserOtpActiveService service;

    @Autowired
    public UserOtpActivePostController(UserOtpActiveService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void execute(UserOtpActiveReqDto userOtpActiveReq) {
        service.execute(userOtpActiveReq);
    }
}
