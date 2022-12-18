package com.training.exception;

public class AttachmentUploadException extends RuntimeException {
    public AttachmentUploadException() {
        super();
    }

    public AttachmentUploadException(String message) {
        super(message);
    }

    public AttachmentUploadException(Throwable cause) {
        super(cause);
    }
}
