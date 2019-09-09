package com.joshbridge.webfluxdemo.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author josh.bridge
 */
@AllArgsConstructor @Getter
public class Employee {

    private final Integer id;

    private final String name;

    private final Long salary;

}
