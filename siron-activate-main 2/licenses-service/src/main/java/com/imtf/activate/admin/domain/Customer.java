package com.imtf.activate.admin.domain;

import java.util.UUID;

public record Customer(UUID oid, String name) {

    public Customer(String name) {
        this(UUID.randomUUID(), name);
    }
}
