package org.aibles.failwall.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResDto {
    private String accessToken;
    private boolean isActivated;
    private static final String tokenType = "Bearer";
}
