package org.aibles.failwall.validation.opt;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {OtpConstraint.OtpValidator.class})
@Target( { ElementType.METHOD, ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface OtpConstraint {
    String message() default "Otp must have 6 digit";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    class OtpValidator implements ConstraintValidator<OtpConstraint, String> {

        @Override
        public void initialize(OtpConstraint constraintAnnotation) {
        }

        @Override
        public boolean isValid(String otp, ConstraintValidatorContext constraintValidatorContext) {
            return otp.length() == 6 && otp.matches("[0-9]+") ;
        }
    }
}
