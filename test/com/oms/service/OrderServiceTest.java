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
import com.oms.service.InventoryService;
import com.oms.service.OrderService;

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
    // TEST: createOrder()
    // --------------------------------------------------------
    @Test
    public void testCreateOrder() {
        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");
        Order order = new OfflineOrder(10, c, "Store A");

        doNothing().when(orderRepo).addOrder(order);

        Order created = orderService.createOrder(order);

        assertEquals(order, created);
        verify(orderRepo, times(1)).addOrder(order);
    }

    // --------------------------------------------------------
    // TEST: addItemToOrder()
    // --------------------------------------------------------
    @Test
    public void testAddItemToOrder_newItem() throws OMSException {

        Product p = new Product("P1", "Laptop", 50000);
        OrderItem item = new OrderItem(p, 2);

        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");
        Order order = new OfflineOrder(10, c, "StoreA");

        when(orderRepo.getOrder(10)).thenReturn(order);
        doNothing().when(inventory).checkStock("P1", 2);

        orderService.addItemToOrder(10, item);

        assertEquals(1, order.getItems().size());
        assertEquals(2, order.getItems().get(0).getQuantity());
    }

    @Test
    public void testAddItemToOrder_mergeQuantity() throws OMSException {

        Product p = new Product("P1", "Laptop", 50000);
        OrderItem existing = new OrderItem(p, 1);
        OrderItem newItem = new OrderItem(p, 3);

        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");
        Order order = new OfflineOrder(10, c, "StoreA");
        order.addItem(existing);

        when(orderRepo.getOrder(10)).thenReturn(order);

        doNothing().when(inventory).checkStock("P1", 4);

        orderService.addItemToOrder(10, newItem);

        assertEquals(4, existing.getQuantity());
    }

    // --------------------------------------------------------
    // TEST: completeOrder()
    // --------------------------------------------------------
    @Test
    public void testCompleteOrder_success() throws OMSException {

        Customer c = new Customer(101, "Priya", "pri@gmail.com", "999", "BLR");

        Product p = new Product("P1", "Laptop", 50000);
        OrderItem item = new OrderItem(p, 2);

        OnlineOrder order = new OnlineOrder(10, c, "Bangalore", 100);
        order.addItem(item);

        when(orderRepo.getOrder(10)).thenReturn(order);

        // Stock checks
        doNothing().when(inventory).checkStock("P1", 2);
        doNothing().when(inventory).reduceStock("P1", 2);

        // Invoice generation
        when(invoiceRepo.generateInvoiceId()).thenReturn(501);
        doNothing().when(invoiceRepo).addInvoice(any(Invoice.class));

        Invoice invoice = orderService.completeOrder(10);

        assertNotNull(invoice);
        assertEquals(501, invoice.getInvoiceId());
        assertEquals(10, invoice.getOrderId());
        assertEquals(100100, invoice.getTotalAmount(), 0.01);

        assertTrue(order.isInvoiceGenerated());
    }

    // --------------------------------------------------------
    // TEST: updateDeliveryStatus()
    // --------------------------------------------------------
    @Test
    public void testUpdateDeliveryStatus() throws OMSException {

        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");

        OnlineOrder order = spy(new OnlineOrder(10, c, "Bangalore", 100));

        when(orderRepo.getOrder(10)).thenReturn(order);

        orderService.updateDeliveryStatus(10, "SHIPPED");

        verify(order, times(1)).updateDeliveryStatus("SHIPPED");
    }

    // --------------------------------------------------------
    // TEST: getAllOrders()
    // --------------------------------------------------------
    @Test
    public void testGetAllOrders_success() throws NoOrdersException {

        Map<Integer, Order> map = new HashMap<>();
        Customer c = new Customer(1, "Priya", "p@gmail.com", "9999", "BLR");
        map.put(1, new OfflineOrder(1, c, "StoreX"));

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
