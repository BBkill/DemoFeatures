package org.aibles.failwall.user.service;

import org.aibles.failwall.user.dto.request.RegisterReqDto;
import org.aibles.failwall.user.dto.response.RegisterResDto;

public interface UserRegisterService {

    RegisterResDto execute(RegisterReqDto registerReq);

}
