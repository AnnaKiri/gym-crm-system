package com.kirillova.gymcrmsystem.util;

import com.kirillova.gymcrmsystem.HasId;
import com.kirillova.gymcrmsystem.error.IllegalRequestDataException;
import com.kirillova.gymcrmsystem.models.User;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

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

    public static void checkNew(HasId bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(HasId bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }
}
