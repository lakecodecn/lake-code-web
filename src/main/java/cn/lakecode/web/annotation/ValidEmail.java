package cn.lakecode.web.annotation;

import cn.lakecode.web.constraint.ValidEmailConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = ValidEmailConstraint.class
)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEmail {

    String value() default "";

    String message() default "{valid email fail}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
