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
    	o.calculateTotal();
        orderRepo.addOrder(o);
        return o;
    }

    
    public void addItemToOrder(int orderId, OrderItem item) throws OMSException {

        Order order = orderRepo.getOrder(orderId);

        if (order == null)
            throw new OMSException("Order not found!");

        if (order.isCompleted())
            throw new OMSException("Cannot add items! Order is already completed.");

        // Stock validation
        if (!inventory.hasStock(item.getProduct().getProductId(), item.getQuantity()))
            throw new OMSException("Not enough stock!");

        order.addItem(item);
        order.getTotalAmount();
        
        order.calculateTotal();

    }

    public Invoice completeOrder(int orderId) throws OMSException {

        Order order = orderRepo.getOrder(orderId);
        if (order == null) throw new OMSException("Order not found!");

        if (order.isCompleted())
            throw new OMSException("Order already completed!");
        
        if (order.isInvoiceGenerated()) {
            throw new OMSException("Order already completed! Invoice already generated.");
        }

        // Reduce stock now
        for (OrderItem item : order.getItems()) {
            inventory.reduceStock(item.getProduct().getProductId(), item.getQuantity());
        }

        order.fulfillOrder();
        order.setInvoiceGenerated(true);
        order.setCompleted(true);

        
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
