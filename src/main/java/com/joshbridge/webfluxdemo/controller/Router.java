package com.joshbridge.webfluxdemo.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RequestPredicates.contentType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * @author josh.bridge
 */
@Configuration
public class Router {

    private static final RequestPredicate ACCEPT_JSON = accept(APPLICATION_JSON);

    private static final RequestPredicate JSON_CONTENT = contentType(APPLICATION_JSON);

    private static final RequestPredicate GET_ONE = GET("/employees/{id}").and(JSON_CONTENT);

    private static final RequestPredicate GET_ALL = GET("/employees").and(JSON_CONTENT);

    private static final RequestPredicate CREATE = POST("/employees/{id}").and(JSON_CONTENT).and(ACCEPT_JSON);

    private static final RequestPredicate DELETE = DELETE("/employees/{id}").and(JSON_CONTENT);

    private static final RequestPredicate ERROR = GET("/error").and(JSON_CONTENT);

    @Bean
    public RouterFunction<ServerResponse> route(EmployeeHandler employeeHandler) {
        return RouterFunctions
                .route(GET_ONE, employeeHandler::findById)
                .andRoute(GET_ALL, employeeHandler::getAll)
                .andRoute(CREATE, employeeHandler::create)
                .andRoute(DELETE, employeeHandler::delete)
                .andRoute(ERROR, employeeHandler::error);
    }
}
