package com.joshbridge.webfluxdemo.exception.handler;

import com.joshbridge.webfluxdemo.types.ErrorResponse;

/**
 * @author josh.bridge
 */
public abstract class ExceptionResponseMapper<T extends Throwable> {

    private final Class<T> exceptionClass;

    public ExceptionResponseMapper(Class<T> exceptionClass) {
        this.exceptionClass = exceptionClass;
    }

    public Class<T> getExceptionClass() {
        return exceptionClass;
    }

    public abstract ErrorResponse map(T ex);

}
