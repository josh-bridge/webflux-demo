package com.joshbridge.webfluxdemo.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;

import java.net.URI;

import org.reactivestreams.Publisher;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.joshbridge.webfluxdemo.exception.DataNotFoundException;

import reactor.core.publisher.Mono;

/**
 * @author josh.bridge
 */
public class Response {

    public static ServerResponse.BodyBuilder ok() {
        return ServerResponse.ok().contentType(APPLICATION_JSON);
    }

    public static <T> Mono<ServerResponse> ok(Publisher<T> body, Class<T> clazz) {
        return ok().body(body, clazz);
    }

    public static <T> Mono<ServerResponse> created(Publisher<T> body, Class<T> clazz, URI location) {
        return ServerResponse.created(location).body(body, clazz);
    }

    public static Mono<ServerResponse> ok(Publisher<Void> voidPublisher) {
        return ok().build(voidPublisher);
    }

    public static <T> Mono<ServerResponse> okOrNotFound(Mono<T> body, Class<T> clazz) {
        final Mono<T> bodyOrError = body.switchIfEmpty(Mono.error(new DataNotFoundException()));

        return ok(bodyOrError, clazz);
    }
}
