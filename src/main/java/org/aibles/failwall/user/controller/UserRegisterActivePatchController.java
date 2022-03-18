package org.aibles.failwall.user.controller;


import org.aibles.failwall.user.dto.request.UserRegisterActiveReqDto;
import org.aibles.failwall.user.service.UserRegisterActiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequestMapping("/api/v1/register/active")
@RestController
public class UserRegisterActivePatchController {

    private final UserRegisterActiveService service;

    @Autowired
    public UserRegisterActivePatchController(UserRegisterActiveService service) {
        this.service = service;
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void execute(@Valid @RequestBody UserRegisterActiveReqDto userRegisterActiveReq) {
        service.execute(userRegisterActiveReq);
    }
}
