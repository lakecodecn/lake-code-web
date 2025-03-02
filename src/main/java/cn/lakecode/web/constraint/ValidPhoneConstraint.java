package cn.lakecode.web.constraint;


import cn.lakecode.web.annotation.ValidPhone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class ValidPhoneConstraint implements ConstraintValidator<ValidPhone, String> {

    private final static Pattern p = Pattern.compile("^1\\d10$");

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) {
            return true;
        }
        return p.matcher(s).find();
    }
}
