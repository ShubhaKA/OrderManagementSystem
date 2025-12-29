package com.oms.repository;

import java.util.HashMap;
import java.util.Map;

import com.oms.model.Order;

public class OrderRepository {

    private Map<Integer, Order> orders = new HashMap<>();
    private int nextOrderId = 1;

    public void addOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }

    public Order getOrder(int id) {
        return orders.get(id);
    }

    public Map<Integer, Order> getAllOrders() {
        return orders;
    }

    public int generateOrderId() {
        return nextOrderId++;
    }
}
