package com.joshbridge.webfluxdemo.exception.handler;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import com.joshbridge.webfluxdemo.types.ErrorResponse;

/**
 * @author josh.bridge
 */
@Component
public class ErrorResponseHandler extends DefaultErrorAttributes {

    private final ErrorResponseComposer errorResponseComposer;

    public ErrorResponseHandler(ErrorResponseComposer errorResponseComposer) {
        this.errorResponseComposer = errorResponseComposer;
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        final Throwable exception = getError(request);

        return getErrorAttributes(exception);
    }

    private Map<String, Object> getErrorAttributes(Throwable exception) {
        final ErrorResponse response = errorResponseComposer.compose(exception);

        return getErrorAttributes(response);
    }

    private Map<String, Object> getErrorAttributes(ErrorResponse response) {
        final Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("id", response.getId());
        errorAttributes.put("error", response.getError());
        errorAttributes.put("dateTime", response.getDateTime().format(ISO_OFFSET_DATE_TIME));
        errorAttributes.put("status", response.getStatus());
        errorAttributes.put("description", response.getDescription());

        return errorAttributes;
    }
}
