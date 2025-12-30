package com.oms.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.oms.model.Customer;
import com.oms.model.OfflineOrder;
import com.oms.model.Order;
import com.oms.repository.OrderRepository;

class OrderRepositoryTest {

    @Test
    void testAddAndGetOrder() {
        OrderRepository repo = new OrderRepository();

        Customer c = new Customer(1, "A", "a@a.com", "999", "X");
        Order order = new OfflineOrder(10, c, "Store A");

        repo.addOrder(order);

        Order fetched = repo.getOrder(10);

        assertNotNull(fetched);
        assertEquals(10, fetched.getOrderId());
    }

    @Test
    void testGetAllOrders() {
        OrderRepository repo = new OrderRepository();

        Customer c = new Customer(1, "A", "a@a.com", "999", "X");

        repo.addOrder(new OfflineOrder(10, c, "Store A"));
        repo.addOrder(new OfflineOrder(20, c, "Store B"));

        assertEquals(2, repo.getAllOrders().size());
    }

    @Test
    void testGenerateOrderId() {
        OrderRepository repo = new OrderRepository();

        int id1 = repo.generateOrderId();
        int id2 = repo.generateOrderId();

        assertEquals(id1 + 1, id2);
    }
}
