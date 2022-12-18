package com.training.exception;

public class AttachmentNotFoundException extends RuntimeException {
    public AttachmentNotFoundException() {
        super();
    }

    public AttachmentNotFoundException(String message) {
        super(message);
    }

    public AttachmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public AttachmentNotFoundException(Throwable cause) {
        super(cause);
    }
}
