package org.aibles.failwall.user.service;

import org.aibles.failwall.user.dto.request.PasswordResetReqDTO;

public interface PasswordResetUpdatePassService {

    void execute(PasswordResetReqDTO passwordResetReq);

}
