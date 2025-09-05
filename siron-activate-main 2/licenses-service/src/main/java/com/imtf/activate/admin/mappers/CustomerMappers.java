package com.imtf.activate.admin.mappers;

import com.imtf.activate.admin.domain.Customer;
import com.imtf.activate.admin.persistence.internal.CustomerDocument;
import com.imtf.activate.admin.rest.v1.resources.CustomerResource;

import java.util.UUID;

public final class CustomerMappers {

    public CustomerResource toResource(Customer customer) {
        return new CustomerResource(customer.oid().toString(), customer.name());
    }

    public Customer fromResource(CustomerResource resource) {
        UUID oid = resource.oid() == null
                ? UUID.randomUUID()
                : UUID.fromString(resource.oid());
        return new Customer(oid, resource.name());
    }

    public CustomerDocument toDocument(Customer customer) {
        return new CustomerDocument(customer.oid().toString(), customer.name());
    }

    public Customer fromDocument(CustomerDocument document) {
        return new Customer(UUID.fromString(document.oid()), document.name());
    }
}
