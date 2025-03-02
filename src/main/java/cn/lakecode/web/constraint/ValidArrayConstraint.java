package cn.lakecode.web.constraint;


import cn.lakecode.web.annotation.ValidArray;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidArrayConstraint implements ConstraintValidator<ValidArray, Integer> {

    private int[] passArray;

    @Override
    public void initialize(ValidArray validArray) {
        passArray = validArray.value();
    }

    @Override
    public boolean isValid(Integer target, ConstraintValidatorContext constraintValidatorContext) {
        if (target == null) {
            return true;
        }
        if (passArray == null) {
            return true;
        }
        for (int i : passArray) {
            if (i == target) {
                return true;
            }
        }
        return false;
    }
}
