package com.imtf.activate.admin.service.exceptions;

public final class DuplicateCapabilityException extends RuntimeException {

    public DuplicateCapabilityException(String capabilityName) {
        super("Capability '" + capabilityName + "' already exists");
    }
}
