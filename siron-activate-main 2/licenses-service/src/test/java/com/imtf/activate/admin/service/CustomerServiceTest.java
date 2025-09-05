package com.imtf.activate.admin.service;

import com.imtf.activate.admin.domain.Customer;
import com.imtf.activate.admin.service.exceptions.DuplicateCustomerException;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class CustomerServiceTest {

    @Inject
    CustomerService service;

    @BeforeEach
    void cleanup() {
        service.getAll().forEach(c -> service.delete(c.oid().toString()));
    }

    @Test
    void getAll_noCustomer_returnEmptyList() {
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void getAll_createCustomer_returnCreatedCustomers() {
        Customer ceb = new Customer("ceb");
        Customer raiffeisen = new Customer("raiffeisen");
        Customer equifax = new Customer("equifax");
        service.create(ceb);
        service.create(raiffeisen);
        service.create(equifax);

        assertEquals(3, service.getAll().size());
        List<Customer> customers = service.getAll();
        for (Customer customer : customers) {
            if ("ceb".equals(customer.name())) {
                assertEquals(ceb, customer);
            } else if ("raiffeisen".equals(customer.name())) {
                assertEquals(raiffeisen, customer);
            } else if ("equifax".equals(customer.name())) {
                assertEquals(equifax, customer);
            }
        }
    }

    @Test
    void create_addCustomer_returnAddedCustomer() {
        Customer ceb = new Customer("ceb");
        Customer createdCustomer = service.create(ceb);
        assertEquals(ceb, createdCustomer);
    }

    @Test
    void create_addCustomerWithUUID_returnAddedCustomer() {
        Customer ceb = new Customer(UUID.randomUUID(), "ceb");
        Customer createdCustomer = service.create(ceb);
        assertEquals(ceb, createdCustomer);
    }

    @Test
    void update_modifyCustomerName_returnModifiedCustomer() {
        Customer customer = new Customer("ceb");
        Customer createdCustomer = service.create(customer);
        assertEquals("ceb", createdCustomer.name());
        Customer updatedCustomer = service.update(createdCustomer.oid().toString(), new Customer("raiffeisen"));
        assertEquals("raiffeisen", updatedCustomer.name());
    }

    @Test
    void delete_createAndDelete_returnEmptyCustomers() {
        Customer customer = new Customer("ceb");
        service.create(customer);
        assertFalse(service.getAll().isEmpty());
        service.delete(customer.oid().toString());
        assertTrue(service.getAll().isEmpty());
    }

    @Test
    void create_twice_errorReturned() {
        Customer customer = new Customer("ubs");
        service.create(customer);
        assertThrows(DuplicateCustomerException.class, () -> service.create(customer));
    }
}