package com.training.service;

import org.springframework.validation.Errors;

public interface ErrorsHandlerService {
    void checkErrors(Errors errors);
}
