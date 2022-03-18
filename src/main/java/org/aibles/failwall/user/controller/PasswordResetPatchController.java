package org.aibles.failwall.user.controller;

import org.aibles.failwall.user.dto.request.PasswordResetReqDTO;
import org.aibles.failwall.user.service.PasswordResetUpdatePassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users/forgot-password")
public class PasswordResetPatchController {

    private final PasswordResetUpdatePassService service;

    @Autowired
    public PasswordResetPatchController(PasswordResetUpdatePassService service) {
        this.service = service;
    }

    @PatchMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void execute(@RequestBody @Valid PasswordResetReqDTO passwordResetReq){
        service.execute(passwordResetReq);
    }

}
