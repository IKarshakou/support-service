package com.training.service.impl;

import com.training.service.ErrorsHandlerService;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;

import java.util.Objects;

@Service
public class ErrorsHandlerServiceImpl implements ErrorsHandlerService {

    @Override
    public void checkErrors(Errors errors) {
        if (errors.hasFieldErrors()) {
            throw new IllegalArgumentException(Objects.requireNonNull(errors.getFieldError()).getDefaultMessage());
        }
    }
}
