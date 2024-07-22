package com.kirillova.gymcrmsystem.util;

import com.kirillova.gymcrmsystem.models.User;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class ValidationUtil {

    private static final Validator validator;

    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private ValidationUtil() {
    }

    private static <T> void validateConstraints(Set<ConstraintViolation<T>> violations) {
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public static <T> void validate(T bean) {
        Set<ConstraintViolation<T>> violations = validator.validate(bean);
        validateConstraints(violations);
    }

    public static void validatePassword(String password) {
        Set<ConstraintViolation<User>> violations = validator.validateValue(User.class, "password", password);
        validateConstraints(violations);
    }
}
