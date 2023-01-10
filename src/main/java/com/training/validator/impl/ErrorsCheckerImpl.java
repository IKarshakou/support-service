package com.training.validator.impl;

import com.training.validator.ErrorsChecker;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Objects;

@Component
public class ErrorsCheckerImpl implements ErrorsChecker {

    @Override
    public void checkErrors(Errors errors) {
        if (errors.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
        }
    }
}
