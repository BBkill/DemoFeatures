package org.aibles.failwall.exception;

import org.springframework.http.HttpStatus;

public class FailWallSystemException extends FailWallAbstractException {
    public FailWallSystemException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
