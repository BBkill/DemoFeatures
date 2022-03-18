package org.aibles.failwall.user.controller;

import org.aibles.failwall.user.dto.request.RegisterReqDto;
import org.aibles.failwall.user.dto.response.RegisterResDto;
import org.aibles.failwall.user.service.UserRegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/register")
public class UserRegisterPostController {

    private final UserRegisterService service;

    @Autowired
    public UserRegisterPostController(UserRegisterService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResDto execute(@RequestBody @Valid RegisterReqDto registerReq) {
        return service.execute(registerReq);
    }
}
