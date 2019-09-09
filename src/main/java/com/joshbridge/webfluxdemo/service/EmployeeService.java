package com.joshbridge.webfluxdemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.joshbridge.webfluxdemo.exception.DataNotFoundException;
import com.joshbridge.webfluxdemo.types.Employee;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author josh.bridge
 */
@Service
public class EmployeeService {

    private static final List<Employee> EMPLOYEES = new ArrayList<>();

    static {
        EMPLOYEES.add(new Employee(1, "Bob", 123456L));
        EMPLOYEES.add(new Employee(1, "John", 654321L));
        EMPLOYEES.add(new Employee(1, "Chris", 1256L));
        EMPLOYEES.add(new Employee(1, "Dom", 1236L));
        EMPLOYEES.add(new Employee(1, "Josh", 3456L));
        EMPLOYEES.add(new Employee(1, "Harry", 156L));
    }

    public Mono<Employee> create(Employee employee) {
        EMPLOYEES.add(employee);

        return Mono.just(employee);
    }

    public Mono<Employee> findById(Integer id) {
        return Flux.fromIterable(EMPLOYEES)
                .filter(emp -> emp.getId().equals(id))
                .next();
    }

    public Flux<Employee> findAll() {
        return Flux.fromIterable(EMPLOYEES);
    }

    public Mono<Employee> update(Employee e) {
        final Optional<Employee> employee = getEmployee(e.getId());

        if (employee.isPresent()) {
            EMPLOYEES.remove(employee.get());
            EMPLOYEES.add(e);

            return Mono.just(e);
        }

        return Mono.empty();
    }

    public Mono<Void> delete(Integer id) {
        final Optional<Employee> employee = getEmployee(id);

        employee.ifPresent(EMPLOYEES::remove);

        return Mono.empty();
    }

    public Mono<Employee> error() {
        return Mono.error(new DataNotFoundException());
    }

    private Optional<Employee> getEmployee(Integer id) {
        return EMPLOYEES.stream()
                .filter(emp -> emp.getId().equals(id))
                .findFirst();
    }

}
