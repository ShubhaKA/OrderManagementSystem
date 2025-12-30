package com.oms.test;

import static org.junit.jupiter.api.Assertions.*;

import com.oms.exception.OMSException;
import com.oms.model.Customer;
import com.oms.model.Invoice;
import com.oms.model.OnlineOrder;
import com.oms.model.OrderItem;
import com.oms.model.Product;
import com.oms.repository.InventoryRepository;
import com.oms.service.InventoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OrderFlowE2ETest {

    Customer customer;
    OnlineOrder order;
    InventoryRepository inventoryRepo;
    InventoryService inventoryService;

    Product p1;

    @BeforeEach
    void setup() {
        customer = new Customer(101, "Priya", "p@gmail.com", "999", "BLR");
        order = new OnlineOrder(1, customer, "Bangalore", 50);

        inventoryRepo = new InventoryRepository();
        inventoryService = new InventoryService(inventoryRepo);

        // preload stock
        p1 = new Product("P1", "Pen", 10);
        inventoryRepo.addStock("P1", 5);  // available stock = 5
    }

    @Test
    void testEndToEndOrderFlow() throws OMSException {
        
        // Step 1: Check stock before adding
        inventoryService.checkStock("P1", 2);

        // Step 2: Add item
        order.addItem(new OrderItem(p1, 2));

        // Step 3: Calculate total
        order.calculateTotal();
        assertEquals(70, order.getTotalAmount()); // 2*10 + 50 shipping

        // Step 4: Reduce stock AFTER confirming the order
        inventoryService.reduceStock("P1", 2);
        assertEquals(3, inventoryRepo.getStock("P1"));

        // Step 5: Fulfill order
        order.fulfillOrder(); 
        assertEquals("PACKED", order.getStatus());

        order.fulfillOrder(); 
        assertEquals("SHIPPED", order.getStatus());

        order.fulfillOrder(); 
        assertEquals("DELIVERED", order.getStatus());
        assertTrue(order.isCompleted());

        // Step 6: Generate invoice
        Invoice invoice = new Invoice(
                1001,
                order.getOrderId(),
                order.getTotalAmount(),
                customer.getCustomerId(),
                customer.getName(),
                customer.getPhone(),
                customer.getEmail(),
                order.getDeliveryAddress(),
                order.getShippingCharge()
        );

        assertEquals(1001, invoice.getInvoiceId());
        assertEquals(order.getTotalAmount(), invoice.getTotalAmount());

        // Step 7: Delivery tracking
        order.updateDeliveryStatus("SHIPPED");
        assertEquals("SHIPPED", order.getDeliveryStatus());

        order.updateDeliveryStatus("DELIVERED");
        assertEquals("DELIVERED", order.getDeliveryStatus());
        assertTrue(order.isCompleted());
    }
}
