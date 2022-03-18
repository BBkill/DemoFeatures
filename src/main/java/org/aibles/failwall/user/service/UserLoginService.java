package org.aibles.failwall.user.service;

import org.aibles.failwall.user.dto.request.LoginReqDto;
import org.aibles.failwall.user.dto.response.LoginResDto;

public interface UserLoginService {
    LoginResDto execute(LoginReqDto loginReq);
}
