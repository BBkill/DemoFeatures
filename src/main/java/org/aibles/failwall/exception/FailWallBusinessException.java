package org.aibles.failwall.exception;

import org.springframework.http.HttpStatus;

public class FailWallBusinessException extends FailWallAbstractException{

    private static final long serialVersionUID = -817462134l;

    public FailWallBusinessException(Object errorMessage, HttpStatus httpStatus) {
        super(errorMessage, httpStatus);
    }
}
