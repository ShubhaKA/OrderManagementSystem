package com.oms.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OrderItemTest {

    @Test
    void testSubtotalCalculation() {
        Product p = new Product("P01", "Pen", 10.0);
        OrderItem item = new OrderItem(p, 5);

        assertEquals(50.0, item.getSubtotal());
    }

    @Test
    void testSetQuantity() {
        Product p = new Product("P01", "Pen", 10.0);
        OrderItem item = new OrderItem(p, 2);

        item.setQuantity(10);

        assertEquals(10, item.getQuantity());
        assertEquals(100.0, item.getSubtotal());
    }
}
