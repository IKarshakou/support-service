package com.training.controller;

import com.training.exception.AttachmentNotFoundException;
import com.training.exception.AttachmentUploadException;
import com.training.exception.TicketNotAvailableException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(ExceptionHandlerController.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> entityNotFound(EntityNotFoundException ex, WebRequest request) {
        log.info(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<Object> entityIsAlreadyExists(EntityExistsException ex, WebRequest request) {
        log.info(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> invalidJwtToken(BadCredentialsException ex, WebRequest request) {
        log.info(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> invalidInputData(IllegalArgumentException ex, WebRequest request) {
        log.info(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(TicketNotAvailableException.class)
    public ResponseEntity<Object> ticketNotAvailable(TicketNotAvailableException ex, WebRequest request) {
        log.info(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN, request);
    }

    @ExceptionHandler(AttachmentUploadException.class)
    public ResponseEntity<Object> ticketNotAvailable(AttachmentUploadException ex, WebRequest request) {
        log.info(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> exceededMaxFileUploadSize(MaxUploadSizeExceededException ex, WebRequest request) {
        log.info(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }

    @ExceptionHandler(AttachmentNotFoundException.class)
    public ResponseEntity<Object> fileNotFound(AttachmentNotFoundException ex, WebRequest request) {
        log.info(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.EXPECTATION_FAILED, request);
    }

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<Object> mailNotSend(MailSendException ex, WebRequest request) {
        log.info(ex.toString());
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR,
                request);
    }
}
