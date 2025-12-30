package com.oms.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.oms.model.Customer;
import com.oms.repository.CustomerRepository;

class CustomerRepositoryTest {

    @Test
    void testAddAndGetCustomer() {
        CustomerRepository repo = new CustomerRepository();

        Customer c = new Customer(101, "Priya", "p@gmail.com", "999", "BLR");
        repo.addCustomer(c);

        Customer result = repo.getCustomer(101);

        assertNotNull(result);
        assertEquals("Priya", result.getName());
    }

    @Test
    void testGetAllCustomers() {
        CustomerRepository repo = new CustomerRepository();

        repo.addCustomer(new Customer(1, "A", "a@a.com", "111", "X"));
        repo.addCustomer(new Customer(2, "B", "b@b.com", "222", "Y"));

        assertEquals(2, repo.getAllCustomers().size());
    }
}
