package com.oms.service;

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

    // Create order
    public Order createOrder(Order o) {
        orderRepo.addOrder(o);
        return o;
    }

    // Add item to existing order
    public void addItemToOrder(int orderId, OrderItem item) throws OMSException {
        Order order = orderRepo.getOrder(orderId);
        if (order == null)
            throw new OMSException("Order not found!");

        inventory.checkStock(
                item.getProduct().getProductId(),
                item.getQuantity()
        );

        order.addItem(item);
    }

    // Complete order and generate invoice
    public Invoice completeOrder(int orderId) throws OMSException {

        Order order = orderRepo.getOrder(orderId);
        if (order == null)
            throw new OMSException("Order not found!");

        // Step 1: validate stock
        for (OrderItem item : order.getItems()) {
            inventory.checkStock(
                    item.getProduct().getProductId(),
                    item.getQuantity()
            );
        }

        // Step 2: reduce stock
        for (OrderItem item : order.getItems()) {
            inventory.reduceStock(
                    item.getProduct().getProductId(),
                    item.getQuantity()
            );
        }

        // Step 3: finalize order
        order.calculateTotal();
        order.fulfillOrder();

        // Step 4: generate invoice
        Invoice invoice = new Invoice(
                invoiceRepo.generateInvoiceId(),
                orderId,
                order.getTotalAmount()
        );

        invoiceRepo.addInvoice(invoice);
        return invoice;
    }
}
