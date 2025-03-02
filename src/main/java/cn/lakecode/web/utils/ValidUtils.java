package cn.lakecode.web.utils;


import cn.lakecode.web.exception.RException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

public class ValidUtils {

    /**
     * 参数校验
     *
     * @param m   model
     * @param <T> type
     */
    public static <T> void valid(T m) {
        valid(m, null);
    }

    /**
     * 参数校验
     *
     * @param m   model
     * @param <T> type
     */
    public static <T, G> void valid(T m, Class<G> clazz) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<T>> violations;
        if (clazz == null) {
            violations = validator.validate(m);
        } else {
            violations = validator.validate(m, clazz);
        }
        for (ConstraintViolation<T> violation : violations) {
            throw new RException(violation.getMessage());
        }
    }

    public static boolean validNull(Object... obj) {
        for (Object o : obj) {
            if (o == null) {
                return false;
            }
        }
        return true;
    }

}
