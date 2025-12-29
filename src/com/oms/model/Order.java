package com.oms.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public abstract class Order {
	
	protected int orderId;
    protected Customer customer;
    protected List<OrderItem> items;
    protected String status;
    protected double totalAmount;
    protected Date orderDate;
    
	public Order(int orderId, Customer customer) {
		
		this.orderId = orderId;
		this.customer = customer;
		this.items = new ArrayList<>();
		this.status = "CREATED";
		this.orderDate = new Date();
	}

	public int getOrderId() {
		return orderId;
	}

	public Customer getCustomer() {
		return customer;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public String getStatus() {
		return status;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public Date getOrderDate() {
		return orderDate;
	}
	
	//Add item to order
	public void addItem(OrderItem item) {
		items.add(item);
	}
	
	//Calculate total amount
	public void calculateTotal() {
		double total = 0;
		for(OrderItem item : items) {
			total += item.getSubtotal();
		}
		this.totalAmount = total;
	}
	
	// Update order status
    public void updateStatus(String newStatus) {
        this.status = newStatus;
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
