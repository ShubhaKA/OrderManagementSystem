package com.oms.service;

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

    public Order getOrder(int oid) {
        return orderRepo.getOrder(oid);
    }

    public Order createOrder(Order o) {
        orderRepo.addOrder(o);
        return o;
    }

    // Add item to order (merge quantities)
    public void addItemToOrder(int orderId, OrderItem item) throws OMSException {

        Order order = orderRepo.getOrder(orderId);
        if (order == null) throw new OMSException("Order not found!");

        String productId = item.getProduct().getProductId();

        OrderItem existing = null;
        for (OrderItem oi : order.getItems()) {
            if (oi.getProduct().getProductId().equals(productId)) {
                existing = oi;
                break;
            }
        }

        if (existing != null) {
            int newQty = existing.getQuantity() + item.getQuantity();
            inventory.checkStock(productId, newQty);
            existing.setQuantity(newQty);
        } else {
            inventory.checkStock(productId, item.getQuantity());
            order.addItem(item);
        }
    }


    // Complete order + prevent duplicate invoices
    public Invoice completeOrder(int orderId) throws OMSException {

        Order order = orderRepo.getOrder(orderId);
        if (order == null) throw new OMSException("Order not found!");

        // Prevent duplicate invoice
        if (order.isInvoiceGenerated()) {
            if (order instanceof OnlineOrder)
                throw new OMSException("Order already delivered! Invoice already generated.");
            else
                throw new OMSException("Order already completed! Invoice already generated.");
        }

        // Stock checks
        for (OrderItem item : order.getItems()) {
            inventory.checkStock(item.getProduct().getProductId(), item.getQuantity());
        }
        for (OrderItem item : order.getItems()) {
            inventory.reduceStock(item.getProduct().getProductId(), item.getQuantity());
        }

        order.calculateTotal();
        order.fulfillOrder();

        Customer c = order.getCustomer();

        String address = null;
        double shipping = 0;

        if (order instanceof OnlineOrder online) {
            address = online.getDeliveryAddress();
            shipping = online.getShippingCharge();
        }

        Invoice invoice = new Invoice(
                invoiceRepo.generateInvoiceId(),
                orderId,
                order.getTotalAmount(),
                c.getCustomerId(),
                c.getName(),
                c.getPhone(),
                c.getEmail(),
                address,
                shipping
        );

        invoiceRepo.addInvoice(invoice);
        order.setInvoiceGenerated(true);

        return invoice;
    }


    public Map<Integer, Order> getAllOrders() throws NoOrdersException {
        Map<Integer, Order> orders = orderRepo.getAllOrders();

        if (orders == null || orders.isEmpty()) {
            throw new NoOrdersException("No orders available!");
        }
        return orders;
    }

    public void updateDeliveryStatus(int orderId, String status) throws OMSException {

        Order order = orderRepo.getOrder(orderId);
        if (order == null)
            throw new OMSException("Order not found!");

        if (!(order instanceof OnlineOrder))
            throw new OMSException("Delivery status can be updated only for Online Orders!");

        OnlineOrder online = (OnlineOrder) order;
        online.updateDeliveryStatus(status);
    }
}
