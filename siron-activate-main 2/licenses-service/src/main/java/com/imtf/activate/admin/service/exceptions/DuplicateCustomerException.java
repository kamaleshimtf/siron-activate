package com.imtf.activate.admin.service.exceptions;

public final class DuplicateCustomerException extends RuntimeException {

    public DuplicateCustomerException(String customerName) {
        super("Customer '" + customerName + "' already exists");
    }
}
