package com.oms.repository;

import java.util.HashMap;
import java.util.Map;
import com.oms.model.Customer;

public class CustomerRepository {

    private Map<Integer, Customer> customers = new HashMap<>();

    public void addCustomer(Customer c) {
        customers.put(c.getCustomerId(), c);
    }

    public Customer getCustomer(int id) {
        return customers.get(id);
    }

	public Map<Integer, Customer> getAllCustomers() {
		return customers;
	}
}
