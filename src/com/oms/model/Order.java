package com.oms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Order {

    protected int orderId;
    protected Customer customer;
    protected List<OrderItem> items = new ArrayList<>();
    protected String status = "CREATED";
    protected double totalAmount;
    protected Date orderDate = new Date();
    protected List<String> trackingUpdates = new ArrayList<>();

    private boolean invoiceGenerated = false;
    private boolean completed = false;

    public Order(int orderId, Customer customer) {
        this.orderId = orderId;
        this.customer = customer;
    }

    public int getOrderId() { return orderId; }
    public Customer getCustomer() { return customer; }
    public List<OrderItem> getItems() { return items; }
    public String getStatus() { return status; }
    public double getTotalAmount() { return totalAmount; }
    public boolean isInvoiceGenerated() { return invoiceGenerated; }
    public boolean isCompleted() { return completed; }
    public Date getOrderDate() { return orderDate; }

    public void setInvoiceGenerated(boolean invoiceGenerated) {
        this.invoiceGenerated = invoiceGenerated;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void calculateTotal() {
        totalAmount = items.stream()
                .mapToDouble(i -> i.getSubtotal())
                .sum();
    }

    public void updateTotalAmount() {
        calculateTotal();
    }

    public void updateStatus(String newStatus) {
        status = newStatus;
        trackingUpdates.add("Status changed to: " + newStatus);
    }

    public List<String> getTrackingUpdates() {
        return trackingUpdates;
    }

    public abstract void fulfillOrder();

    @Override
    public String toString() {
        return "Order ID: " + orderId +
                ", Customer: " + customer.getName() +
                ", Total: Rs." + totalAmount +
                ", Status: " + status +
                ", Date: " + orderDate;
    }
}
