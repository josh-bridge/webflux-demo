package com.joshbridge.webfluxdemo.exception;

import org.springframework.http.HttpStatus;

/**
 * @author josh.bridge
 */
public class InvalidArgException extends BridgeException {

    private static final String INVARG = "INVARG";

    public InvalidArgException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public InvalidArgException(Throwable cause) {
        super(HttpStatus.BAD_REQUEST, null, cause);
    }

    @Override
    public String getCode() {
        return INVARG;
    }

    @Override
    public String getMessage() {
        return "An invalid argument was passed";
    }
}
