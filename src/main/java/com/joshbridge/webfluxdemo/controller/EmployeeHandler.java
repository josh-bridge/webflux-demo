package com.joshbridge.webfluxdemo.controller;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.joshbridge.webfluxdemo.service.EmployeeService;
import com.joshbridge.webfluxdemo.types.Employee;
import com.joshbridge.webfluxdemo.types.EmployeeResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author josh.bridge
 */
@Component
public class EmployeeHandler {

    private static final List<String> FILTERS = Arrays.asList("name");

    private final EmployeeService employeeService;

    public EmployeeHandler(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @NonNull
    public Mono<ServerResponse> findById(ServerRequest request) {
        final Mono<Integer> id = Mono.just(getId(request));

        final Mono<Employee> result = id.flatMap(employeeService::findById);

        return Response.okOrNotFound(result, Employee.class);
    }

    @NonNull
    public Mono<ServerResponse> getAll(ServerRequest request) {
        final Map<String, String> filterParams = getFilterParams(request);

        final Flux<Employee> employees = employeeService.findAll()
                .filter(matchingName(filterParams));

        final Mono<EmployeeResponse> response = mapResponse(employees);

        return Response.ok(response, EmployeeResponse.class);
    }

    @NonNull
    public Mono<ServerResponse> create(ServerRequest request) {
        final Mono<Employee> employee = request.bodyToMono(Employee.class);

        final Mono<Employee> result = employee.flatMap(employeeService::create);

        return Response.ok(result, Employee.class);
    }

    @NonNull
    public Mono<ServerResponse> delete(ServerRequest request) {
        final Mono<Integer> id = Mono.just(getId(request));

        final Mono<Void> result = id.flatMap(employeeService::delete);

        return Response.ok(result);
    }

    @NonNull
    public Mono<ServerResponse> error(ServerRequest request) {
        final Mono<Employee> id = employeeService.error();

        return Response.okOrNotFound(id, Employee.class);
    }

    private Mono<EmployeeResponse> mapResponse(Flux<Employee> employees) {
        final Mono<List<Employee>> emps = employees.collectList();

        return emps.map(EmployeeResponse::new);
    }

    private Predicate<Employee> matchingName(Map<String, String> filters) {
        return employee -> {
            if (!filters.containsKey("name")) {
                return true;
            }

            final String requestedName = decode(filters.get("name"));
            final String employeeName = employee.getName().toLowerCase();

            return employeeName.contains(requestedName) || requestedName.contains(employeeName);
        };
    }

    private String decode(String name) {
        try {
            return URLDecoder.decode(name, UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private Map<String, String> getFilterParams(ServerRequest request) {
        final MultiValueMap<String, String> params = request.queryParams();
        final Map<String, String> filters = new HashMap<>();

        FILTERS.forEach(filter -> {
            if (params.containsKey(filter)) {
                filters.put(filter, params.get(filter).get(0));
            }
        });

        return filters;
    }

    private Integer getId(ServerRequest request) {
        return Integer.valueOf(request.pathVariable("id"));
    }
}
