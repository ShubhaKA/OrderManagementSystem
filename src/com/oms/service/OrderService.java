package com.oms.service;

import com.oms.exception.OMSException;
import com.oms.model.*;
import com.oms.repository.InvoiceRepository;
import com.oms.repository.OrderRepository;

public class OrderService {

    private InventoryService inventory;
    private OrderRepository orderRepo;
    private InvoiceRepository invoiceRepo;

    public OrderService(InventoryService inventory, OrderRepository orderRepo, InvoiceRepository invoiceRepo) {
        this.inventory = inventory;
        this.orderRepo = orderRepo;
        this.invoiceRepo = invoiceRepo;
    }

    public Order createOrder(Order o) {
        orderRepo.addOrder(o);
        return o;
    }

    public void addItemToOrder(int orderId, OrderItem item) throws OMSException {
        Order order = orderRepo.getOrder(orderId);
        if (order == null) throw new OMSException("Order not found!");
        inventory.checkStock(item.getProduct().getProductId(), item.getQuantity());
        order.addItem(item);
    }

    public Invoice completeOrder(int orderId) throws OMSException {

        Order order = orderRepo.getOrder(orderId);
        if (order == null) throw new OMSException("Order not found!");

        for (OrderItem item : order.getItems()) {
            inventory.checkStock(item.getProduct().getProductId(), item.getQuantity());
        }

        for (OrderItem item : order.getItems()) {
            inventory.reduceStock(item.getProduct().getProductId(), item.getQuantity());
        }

        order.calculateTotal();
        order.fulfillOrder();

        Invoice invoice = new Invoice(
                invoiceRepo.generateInvoiceId(),
                orderId,
                order.getTotalAmount()
        );

        invoiceRepo.addInvoice(invoice);
        return invoice;
    }
}
