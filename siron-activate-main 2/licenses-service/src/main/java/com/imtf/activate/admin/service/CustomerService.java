package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.Action;
import com.imtf.activate.admin.domain.ActionScope;
import com.imtf.activate.admin.domain.Customer;
import com.imtf.activate.admin.domain.Effect;
import com.imtf.activate.admin.persistence.CustomerPersistence;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class CustomerService {

    private final CustomerPersistence persistence;

    private final AuditService auditService;

    @Inject
    public CustomerService(CustomerPersistence persistence, AuditService auditService) {
        this.persistence = persistence;
        this.auditService = auditService;
    }

    public List<Customer> getAll() {
        return persistence.getAll();
    }

    public Customer create(Customer customer) {
        persistence.create(customer);
        auditService.log(new Action(ActionScope.CUSTOMER, Effect.CustomerEffect.CREATE, customer));
        return customer;
    }

    public Customer update(String oid, Customer customer) {
        return persistence.update(oid, customer);
    }

    public void delete(String oid) {
        persistence.delete(oid);
    }
}
