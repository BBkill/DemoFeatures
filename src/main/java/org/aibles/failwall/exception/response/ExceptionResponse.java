package org.aibles.failwall.exception.response;

import java.time.Instant;
import java.time.LocalDateTime;

public class ExceptionResponse {
    private String timestamp;
    private String error;
    private Object message;

    public ExceptionResponse() {
    }

    public void setError(String error) {
        this.error = error;
    }

    public void setMessage(Object message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public Object getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
