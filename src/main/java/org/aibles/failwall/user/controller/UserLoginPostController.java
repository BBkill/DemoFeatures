package org.aibles.failwall.user.controller;

import org.aibles.failwall.user.dto.request.LoginReqDto;
import org.aibles.failwall.user.dto.response.LoginResDto;
import org.aibles.failwall.user.service.UserLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/login")
public class UserLoginPostController {

    private final UserLoginService service;

    @Autowired
    public UserLoginPostController(UserLoginService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public LoginResDto execute(@RequestBody LoginReqDto loginRequestDTO){
        return service.execute(loginRequestDTO);
    }
}
