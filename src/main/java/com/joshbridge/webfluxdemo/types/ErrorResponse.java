package com.joshbridge.webfluxdemo.types;

import java.time.ZonedDateTime;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author josh.bridge
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE) @Getter
public class ErrorResponse {

    public ErrorResponse(String code, String description, Integer status) {
        this(UUID.randomUUID().toString(), ZonedDateTime.now(), code, description, status);
    }

    private final String id;

    private final ZonedDateTime dateTime;

    private final String error;

    private final String description;

    private final Integer status;

}
