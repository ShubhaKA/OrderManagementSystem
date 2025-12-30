package com.oms.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OnlineOrderTest {

    Customer customer;
    OnlineOrder order;

    @BeforeEach
    void setup() {
    	customer  = new Customer(101, "Priya", "p@gmail.com", "999", "BLR");
        order = new OnlineOrder(101, customer, "Bangalore", 50.0);
    }

    @Test
    void testCalculateTotal() {
        Product p1 = new Product("P1", "Pen", 10.0);
        Product p2 = new Product("P2", "Book", 100.0);

        order.addItem(new OrderItem(p1, 2));    // 20
        order.addItem(new OrderItem(p2, 1));    // 100

        order.calculateTotal();

        assertEquals(170.0, order.getTotalAmount());
    }

    @Test
    void testFulfillmentFlow() {
        assertEquals("CREATED", order.getStatus());

        order.fulfillOrder();
        assertEquals("PACKED", order.getStatus());

        order.fulfillOrder();
        assertEquals("SHIPPED", order.getStatus());

        order.fulfillOrder();
        assertEquals("DELIVERED", order.getStatus());
        assertTrue(order.isCompleted());
    }

    @Test
    void testUpdateDeliveryStatus() {

        // Initial
        assertEquals("CREATED", order.getDeliveryStatus());
        assertFalse(order.isCompleted());

        // Step 1: SHIPPED (only delivery status changes, NOT order.status)
        order.updateDeliveryStatus("SHIPPED");

        assertEquals("SHIPPED", order.getDeliveryStatus());
        assertFalse(order.isCompleted());

        // Move order.status forward manually to match deliveryState logic
        order.fulfillOrder(); // CREATED → PACKED
        order.fulfillOrder(); // PACKED → SHIPPED

        assertEquals("SHIPPED", order.getStatus());

        // Step 2: DELIVERED → This should now complete the order
        order.updateDeliveryStatus("DELIVERED");

        assertEquals("DELIVERED", order.getDeliveryStatus());
        assertTrue(order.isCompleted());
        assertEquals("DELIVERED", order.getStatus());
    }

}
