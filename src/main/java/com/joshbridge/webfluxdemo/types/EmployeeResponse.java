package com.joshbridge.webfluxdemo.types;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author josh.bridge
 */
@AllArgsConstructor @Getter
public class EmployeeResponse {

    private final List<Employee> employees;

}
