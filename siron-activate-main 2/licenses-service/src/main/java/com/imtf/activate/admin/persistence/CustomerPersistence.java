package com.imtf.activate.admin.persistence;

import com.imtf.activate.admin.domain.Customer;

import java.util.List;

public interface CustomerPersistence {

    List<Customer> getAll();

    void create(Customer customer);

    Customer update(String oid, Customer customer);

    void delete(String oid);
}
