package com.oms.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class OfflineOrderTest {

    @Test
    void testFulfillOrder() {
        Customer c = new Customer(2, "Priya", "p@gmail.com","8888888888", "Bangalore");
        OfflineOrder order = new OfflineOrder(200, c, "Chennai Store");

        order.fulfillOrder();

        assertEquals("COMPLETED", order.getStatus());
        assertTrue(order.isCompleted());
        assertEquals(3, order.getTrackingUpdates().size());
    }
}
