package com.imtf.activate.admin.domain;

import java.util.List;
import java.util.UUID;

public record Capability(UUID oid, String name, List<CapabilityFeature> features) {

    public Capability(String name, List<CapabilityFeature> features) {
        this(UUID.randomUUID(), name, features);
    }
}