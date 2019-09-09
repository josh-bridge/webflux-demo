package com.joshbridge.webfluxdemo.exception.handler;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.joshbridge.webfluxdemo.exception.BridgeException;
import com.joshbridge.webfluxdemo.types.ErrorResponse;

/**
 * @author josh.bridge
 */
@Order(-1)
@Component
public class BridgeExceptionMapper extends ExceptionResponseMapper<BridgeException> {

    public BridgeExceptionMapper() {
        super(BridgeException.class);
    }

    @Override
    public ErrorResponse map(BridgeException ex) {
        final String code = ex.getCode();
        final String description = ex.getLocalizedMessage();
        final int status = ex.getStatus().value();

        return new ErrorResponse(code, description, status);
    }
}
