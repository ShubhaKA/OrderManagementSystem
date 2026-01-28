package com.oms.service;

import java.util.List;
import java.util.Map;

import com.oms.exception.NoOrdersException;
import com.oms.exception.OMSException;
import com.oms.model.*;
import com.oms.repository.InvoiceRepository;
import com.oms.repository.OrderRepository;

public class OrderService {

    private InventoryService inventory;
    private OrderRepository orderRepo;
    private InvoiceRepository invoiceRepo;

    public OrderService(InventoryService inventory,
                        OrderRepository orderRepo,
                        InvoiceRepository invoiceRepo) {
        this.inventory = inventory;
        this.orderRepo = orderRepo;
        this.invoiceRepo = invoiceRepo;
    }

    // ---------------- GET ORDER ----------------
    public Order getOrder(int oid) {
        return orderRepo.getOrder(oid);
    }

    // ---------------- ALL ORDERS ----------------
    public Map<Integer, Order> getAllOrders() throws NoOrdersException {
        Map<Integer, Order> orders = orderRepo.getAllOrders();
        if (orders.isEmpty())
            throw new NoOrdersException("No orders available!");
        return orders;
    }

    public Invoice createOrderWithItems(Order order, List<OrderItem> items) throws OMSException {

        // 1. Just add items (stock already handled in Main)
        for (OrderItem item : items) {
            order.addItem(item);
        }

        // 2. Calculate total
        order.calculateTotal();

        // 3. Save order
        orderRepo.addOrder(order);

        // 4. OFFLINE → auto complete + invoice
        if (order instanceof OfflineOrder) {
            order.fulfillOrder();
            order.setCompleted(true);

            Customer c = order.getCustomer();
            Invoice invoice = new Invoice(
                    invoiceRepo.generateInvoiceId(),
                    order.getOrderId(),
                    order.getTotalAmount(),
                    c.getCustomerId(),
                    c.getName(),
                    c.getPhone(),
                    c.getEmail(),
                    null,
                    0,
                    order.getItems() 
            );

            invoiceRepo.addInvoice(invoice);
            order.setInvoiceGenerated(true);
            return invoice;
        }

        return null;
    }
    
    public void updateDeliveryStatus(int orderId, String status) throws OMSException {

        Order order = orderRepo.getOrder(orderId);
        if (order == null)
            throw new OMSException("Order not found!");

        if (!(order instanceof OnlineOrder))
            throw new OMSException("Only Online Orders have delivery status!");

        OnlineOrder online = (OnlineOrder) order;
        online.updateDeliveryStatus(status);

        if (status.equalsIgnoreCase("DELIVERED")) {

            if (!order.isInvoiceGenerated()) {

                Customer c = order.getCustomer();

                Invoice invoice = new Invoice(
                        invoiceRepo.generateInvoiceId(),
                        order.getOrderId(),
                        order.getTotalAmount(),
                        c.getCustomerId(),
                        c.getName(),
                        c.getPhone(),
                        c.getEmail(),
                        online.getDeliveryAddress(),
                        online.getShippingCharge(),
                        order.getItems()
                );

                invoiceRepo.addInvoice(invoice);
                order.setInvoiceGenerated(true);

                // ✅ ADD THIS
                System.out.println(
                    "Invoice Generated Successfully! Invoice ID: " +
                    invoice.getInvoiceId()
                );
            }

            order.setCompleted(true);
            System.out.println("Order marked as COMPLETED.");
        }
    }

}
