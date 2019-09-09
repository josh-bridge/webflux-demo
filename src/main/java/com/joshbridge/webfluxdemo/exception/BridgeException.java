package com.joshbridge.webfluxdemo.exception;

import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * @author josh.bridge
 */
public abstract class BridgeException extends ResponseStatusException {

    public BridgeException(HttpStatus status) {
        super(status);
    }

    public BridgeException(HttpStatus status, String reason) {
        super(status, reason);
    }

    public BridgeException(HttpStatus status, String reason, Throwable cause) {
        super(status, reason, cause);
    }

    @Override
    public String getMessage() {
        return NestedExceptionUtils.buildMessage(getReason(), getCause());
    }

    public abstract String getCode();

}
