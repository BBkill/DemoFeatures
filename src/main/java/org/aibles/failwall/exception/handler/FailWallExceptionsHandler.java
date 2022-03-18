package org.aibles.failwall.exception.handler;

import org.aibles.failwall.exception.FailWallAbstractException;
import org.aibles.failwall.exception.FailWallBusinessException;
import org.aibles.failwall.exception.response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class FailWallExceptionsHandler {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler (FailWallAbstractException.class)
    public ResponseEntity<ExceptionResponse> handleCommonException(FailWallAbstractException ex){
        return ResponseEntity.status(ex.getHttpStatus())
                .body(this.getResponse(ex.getErrorMessage(), ex.getHttpStatus()));
    }

    @ExceptionHandler (MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleValidation (MethodArgumentNotValidException ex){
        Map<String,String> errorMap = new HashMap<>();
        for (ObjectError er : ex.getBindingResult().getAllErrors()){
            errorMap.put(((FieldError)er).getField(),er.getDefaultMessage());
        }

        HttpStatus errResponseStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(errResponseStatus)
                .body(this.getResponse(errorMap, errResponseStatus));
    }

    private ExceptionResponse getResponse(Object errorMessage, HttpStatus status) {
        ExceptionResponse response = new ExceptionResponse();
        response.setTimestamp(DATE_TIME_FORMATTER.format(LocalDateTime.now()));
        response.setError(status.toString());
        response.setMessage(errorMessage);

        return response;
    }
}

