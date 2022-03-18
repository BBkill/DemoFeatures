package org.aibles.failwall.user.dto.request;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Builder
@Data
public class RegisterReqDto {

    @NotBlank(message = "User name is required")
    @Length(max = 30, message = "User name's length must be less than or equals to than 30 characters")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    @Length(min = 8, message = "Password's length must be greater than or equals to 8 characters")
    private String password;

    @NotBlank(message = "passwordConfirm is required")
    @Length(min = 8, message = "PasswordConfirm's length must be greater than or equals to 8 characters")
    private String passwordConfirm;
}
