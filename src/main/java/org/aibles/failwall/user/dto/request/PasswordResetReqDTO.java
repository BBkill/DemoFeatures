package org.aibles.failwall.user.dto.request;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class PasswordResetReqDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "New password is required")
    @Size(min = 6, message = "The number characters of new password must be greater than or equal to 6 characters")
    private String newPassword;

    @NotBlank(message = "New password confirmation is required")
    private String passwordConfirm;

    @NotBlank(message = "Password reset key is required")
    private String passwordResetKey;

}
