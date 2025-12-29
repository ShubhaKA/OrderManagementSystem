package com.oms.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class OnlineOrder extends Order {

    private String deliveryAddress;
    private double shippingCharge;
    private String deliveryStatus;
    
    private List<String> trackingUpdates = new ArrayList<>();

    public OnlineOrder(int orderId, Customer customer, String deliveryAddress, double shippingCharge) {
        super(orderId, customer);
        this.deliveryAddress = deliveryAddress;
        this.shippingCharge = shippingCharge;
        this.deliveryStatus = "CREATED";

        trackingUpdates.add("Order Created on " + new Date());
    }

    public String getDeliveryAddress() { return deliveryAddress; }
    public double getShippingCharge() { return shippingCharge; }

    @Override
    public void calculateTotal() {
        super.calculateTotal();
        totalAmount += shippingCharge;
    }
    
    public String getDeliveryStatus() {
        return deliveryStatus;
    }
    
    public List<String> getTrackingUpdates() {
        return trackingUpdates;
    }

    @Override
    public void fulfillOrder() {

        switch (status) {

            case "CREATED":
                updateStatus("PACKED");
                break;

            case "PACKED":
                updateStatus("SHIPPED");
                break;

            case "SHIPPED":
                updateStatus("DELIVERED");
                setCompleted(true);
                break;

            case "DELIVERED":
                System.out.println("Order already delivered.");
                break;
        }
    }


    public void updateDeliveryStatus(String newStatus) {

        this.deliveryStatus = newStatus;

        trackingUpdates.add(
            "Status updated to " + newStatus + " at " + new Date()
        );

        // If delivered → mark as completed
        if (newStatus.equalsIgnoreCase("DELIVERED")) {
            fulfillOrder();   // call child class implementation
        }
    }


    @Override
    public String toString() {
        return super.toString() +
                ", Delivery Address: " + deliveryAddress +
                ", Shipping: Rs." + shippingCharge;
    }
}
