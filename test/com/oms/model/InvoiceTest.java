package com.oms.model;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

class InvoiceTest {

    @Test
    void testInvoiceValuesOnlineOrder() {

        Invoice invoice = new Invoice(
                1001,
                501,
                550.0,
                1,
                "Shubha",
                "9999999999",
                "s@test.com",
                "Bangalore",
                50.0
        );

        assertEquals(1001, invoice.getInvoiceId());
        assertEquals(501, invoice.getOrderId());
        assertEquals(550.0, invoice.getTotalAmount());
        assertEquals("Shubha", invoice.getCustomerName());
        assertEquals("Bangalore", invoice.getDeliveryAddress());
        assertEquals(50.0, invoice.getShippingCharge());
    }

    @Test
    void testInvoiceValuesOfflineOrder() {

        Invoice invoice = new Invoice(
                1002,
                502,
                800.0,
                2,
                "Rahul",
                "8888888888",
                "r@test.com",
                null,       // offline → no delivery address
                0.0
        );

        assertEquals(1002, invoice.getInvoiceId());
        assertEquals(502, invoice.getOrderId());
        assertEquals(800.0, invoice.getTotalAmount());
        assertNull(invoice.getDeliveryAddress());
        assertEquals(0.0, invoice.getShippingCharge());
    }

    @Test
    void testInvoiceWithItems() {

        Product laptop = new Product("101", "Laptop", 55000);
        Product mouse = new Product("102", "Mouse", 800);

        OrderItem item1 = new OrderItem(laptop, 1);
        OrderItem item2 = new OrderItem(mouse, 2);

        List<OrderItem> items = new ArrayList<>();
        items.add(item1);
        items.add(item2);

        Invoice invoice = new Invoice(
                1003,
                503,
                56600.0,
                3,
                "Anu",
                "7777777777",
                "a@test.com",
                "Chennai",
                100.0
        );

        invoice.setItems(items);

        assertEquals(2, invoice.getItems().size());
        assertEquals(55000, invoice.getItems().get(0).getSubtotal());
        assertEquals(1600, invoice.getItems().get(1).getSubtotal());
    }

    @Test
    void testInvoiceToString() {

        Invoice invoice = new Invoice(
                1004,
                504,
                500.0,
                4,
                "Kiran",
                "6666666666",
                "k@test.com",
                null,
                0.0
        );

        String result = invoice.toString();

        assertTrue(result.contains("Invoice ID"));
        assertTrue(result.contains("Order ID"));
        assertTrue(result.contains("Total"));
        assertTrue(result.contains("Kiran"));
    }
}
