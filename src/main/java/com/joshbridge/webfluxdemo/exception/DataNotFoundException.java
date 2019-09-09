package com.joshbridge.webfluxdemo.exception;

import org.springframework.http.HttpStatus;

/**
 * @author josh.bridge
 */
public class DataNotFoundException extends BridgeException {

    private static final String NOTFOUND = "NOTFOUND";

    public DataNotFoundException() {
        super(HttpStatus.NOT_FOUND, null, null);
    }

    public DataNotFoundException(Throwable cause) {
        super(HttpStatus.NOT_FOUND, null, cause);
    }

    @Override
    public String getCode() {
        return NOTFOUND;
    }

    @Override
    public String getMessage() {
        return "The requested resource was not found";
    }
}
