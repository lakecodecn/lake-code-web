package cn.lakecode.web.annotation;


import cn.lakecode.web.constraint.ValidArrayConstraint;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(
        validatedBy = ValidArrayConstraint.class
)
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidArray {

    int[] value() default {};

    String message() default "{valid array fail}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
