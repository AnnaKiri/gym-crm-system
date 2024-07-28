package com.kirillova.gymcrmsystem.util;

import com.kirillova.gymcrmsystem.models.Entity;
import com.kirillova.gymcrmsystem.error.IllegalRequestDataException;
import com.kirillova.gymcrmsystem.error.NotFoundException;
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

    public static void checkNew(Entity bean) {
        if (!bean.isNew()) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must be new (id=null)");
        }
    }

    public static void assureIdConsistent(Entity bean, int id) {
        if (bean.isNew()) {
            bean.setId(id);
        } else if (bean.id() != id) {
            throw new IllegalRequestDataException(bean.getClass().getSimpleName() + " must has id=" + id);
        }
    }

    public static <T> T checkNotFoundWithUsername(T object, String username) {
        checkNotFoundWithUsername(object != null, username);
        return object;
    }

    public static void checkNotFoundWithUsername(boolean found, String username) {
        checkNotFound(found, "username=" + username);
    }

    public static <T> T checkNotFoundWithId(T object, int id) {
        checkNotFoundWithId(object != null, id);
        return object;
    }

    public static void checkNotFoundWithId(boolean found, int id) {
        checkNotFound(found, "id=" + id);
    }

    public static <T> T checkNotFound(T object, String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(boolean found, String msg) {
        if (!found) {
            throw new NotFoundException("Not found entity with " + msg);
        }
    }
}
