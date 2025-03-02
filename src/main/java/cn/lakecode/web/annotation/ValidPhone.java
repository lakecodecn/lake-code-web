package cn.lakecode.web.annotation;

import cn.lakecode.web.constraint.ValidPhoneConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = ValidPhoneConstraint.class
)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPhone {

    String value() default "";

    String message() default "{valid phone fail}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
