package com.training.validator;

import org.springframework.validation.Errors;

public interface ErrorsChecker {
    void checkErrors(Errors errors);
}
