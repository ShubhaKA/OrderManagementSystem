package com.oms.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void testInvoiceValues() {
        Invoice invoice = new Invoice(
                1001,
                501,
                500.0,
                1,
                "Shubha",
                "9999999999",
                "s@test.com",
                "Bangalore",
                50.0
        );

        assertEquals(1001, invoice.getInvoiceId());
        assertEquals(501, invoice.getOrderId());
        assertEquals(500.0, invoice.getTotalAmount());
    }

    @Test
    void testInvoiceString() {
        Invoice invoice = new Invoice(
                1001, 501, 500.0,
                1, "Shubha", "999", "s@test.com",
                null, 0.0
        );

        String result = invoice.toString();
        assertTrue(result.contains("Invoice ID: 1001"));
        assertTrue(result.contains("Order ID: 501"));
        assertTrue(result.contains("Total Amount: Rs. 500.0"));
    }
}
