package org.aibles.failwall.user.dto.response;

import lombok.Builder;

@Builder
public class RegisterResDto {
    private String name;
    private String email;
    private boolean isActivated;
}
