package org.aibles.failwall.exception;

import org.springframework.http.HttpStatus;

public class FailWallAbstractException extends RuntimeException {

    private static final long serialVersionUID = -324324234l;

    private final Object errorMessage;
    private final HttpStatus httpStatus;
    
    public FailWallAbstractException(Object errorMessage, HttpStatus httpStatus) {
        super();
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public Object getErrorMessage() {
        return errorMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
