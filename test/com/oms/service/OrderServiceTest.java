package com.oms.service;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;

import com.oms.exception.NoOrdersException;
import com.oms.exception.OMSException;
import com.oms.model.*;
import com.oms.repository.InvoiceRepository;
import com.oms.repository.OrderRepository;

public class OrderServiceTest {

    private InventoryService inventory;
    private OrderRepository orderRepo;
    private InvoiceRepository invoiceRepo;
    private OrderService orderService;

    @Before
    public void setup() {
        inventory = mock(InventoryService.class);
        orderRepo = mock(OrderRepository.class);
        invoiceRepo = mock(InvoiceRepository.class);
        orderService = new OrderService(inventory, orderRepo, invoiceRepo);
    }

    // --------------------------------------------------------
    // TEST: createOrderWithItems() → OFFLINE ORDER
    // --------------------------------------------------------
    @Test
    public void testCreateOfflineOrderWithItems_generatesInvoice() throws OMSException {

        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");
        Product p = new Product("P1", "Mouse", 500);

        OrderItem item = new OrderItem(p, 2);
        List<OrderItem> items = List.of(item);

        OfflineOrder order = new OfflineOrder(10, c, "Store A");

        when(invoiceRepo.generateInvoiceId()).thenReturn(1001);

        Invoice invoice = orderService.createOrderWithItems(order, items);

        assertNotNull(invoice);
        assertEquals(1001, invoice.getInvoiceId());
        assertEquals(10, invoice.getOrderId());
        assertEquals(1000.0, invoice.getTotalAmount(), 0.01);

        assertTrue(order.isCompleted());
        assertTrue(order.isInvoiceGenerated());

        verify(orderRepo, times(1)).addOrder(order);
        verify(invoiceRepo, times(1)).addInvoice(any(Invoice.class));
    }

    // --------------------------------------------------------
    // TEST: createOrderWithItems() → ONLINE ORDER
    // --------------------------------------------------------
    @Test
    public void testCreateOnlineOrderWithItems_noInvoice() throws OMSException {

        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");
        Product p = new Product("P1", "Laptop", 50000);

        OrderItem item = new OrderItem(p, 1);
        List<OrderItem> items = List.of(item);

        OnlineOrder order = new OnlineOrder(20, c, "Bangalore", 100);

        Invoice invoice = orderService.createOrderWithItems(order, items);

        assertNull(invoice);
        assertFalse(order.isCompleted());
        assertFalse(order.isInvoiceGenerated());

        verify(orderRepo, times(1)).addOrder(order);
    }

    // --------------------------------------------------------
    // TEST: updateDeliveryStatus() → DELIVERED
    // --------------------------------------------------------
    @Test
    public void testUpdateDeliveryStatus_delivered_generatesInvoice() throws OMSException {

        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");
        Product p = new Product("P1", "Laptop", 50000);

        OrderItem item = new OrderItem(p, 1);
        OnlineOrder order = new OnlineOrder(30, c, "Bangalore", 100);
        order.addItem(item);
        order.calculateTotal();

        when(orderRepo.getOrder(30)).thenReturn(order);
        when(invoiceRepo.generateInvoiceId()).thenReturn(2001);

        orderService.updateDeliveryStatus(30, "DELIVERED");

        assertTrue(order.isCompleted());
        assertTrue(order.isInvoiceGenerated());

        verify(invoiceRepo, times(1)).addInvoice(any(Invoice.class));
    }

    // --------------------------------------------------------
    // TEST: updateDeliveryStatus() → NON-ONLINE ORDER
    // --------------------------------------------------------
    @Test(expected = OMSException.class)
    public void testUpdateDeliveryStatus_offlineOrder_throwsException() throws OMSException {

        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");
        Order order = new OfflineOrder(40, c, "Store X");

        when(orderRepo.getOrder(40)).thenReturn(order);

        orderService.updateDeliveryStatus(40, "DELIVERED");
    }

    // --------------------------------------------------------
    // TEST: getAllOrders()
    // --------------------------------------------------------
    @Test
    public void testGetAllOrders_success() throws NoOrdersException {

        Map<Integer, Order> map = new HashMap<>();
        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");
        map.put(1, new OfflineOrder(1, c, "Store X"));

        when(orderRepo.getAllOrders()).thenReturn(map);

        Map<Integer, Order> result = orderService.getAllOrders();

        assertEquals(1, result.size());
    }

    @Test(expected = NoOrdersException.class)
    public void testGetAllOrders_empty() throws NoOrdersException {

        when(orderRepo.getAllOrders()).thenReturn(new HashMap<>());

        orderService.getAllOrders();
    }
}
