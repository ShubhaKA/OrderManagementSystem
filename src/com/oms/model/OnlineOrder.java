package com.oms.model;

import java.util.Date;

public class OnlineOrder extends Order {

    private String deliveryAddress;
    private double shippingCharge;
    private String deliveryStatus = "CREATED";  
    
    public OnlineOrder(int orderId, Customer customer,
                       String deliveryAddress, double shippingCharge) {
        super(orderId, customer);
        this.deliveryAddress = deliveryAddress;
        this.shippingCharge = shippingCharge;
        trackingUpdates.add("Online Order Created on " + new Date());
    }

    public String getDeliveryAddress() { return deliveryAddress; }
    public double getShippingCharge() { return shippingCharge; }
    public String getDeliveryStatus() { return deliveryStatus; }
    
    @Override
    public void calculateTotal() {
        super.calculateTotal();    
        totalAmount += shippingCharge;
    }


    @Override
    public void fulfillOrder() {
    }

    public void updateDeliveryStatus(String newStatus) {

        this.deliveryStatus = newStatus;
        trackingUpdates.add("Status updated to " + newStatus + " at " + new Date());

        // sync main order status
        //updateStatus(newStatus);

        if (newStatus.equalsIgnoreCase("DELIVERED")) {
            setCompleted(true);
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Delivery Address: " + deliveryAddress +
                ", Shipping: Rs." + shippingCharge +
                ", Delivery Status: " + deliveryStatus;
    }
}
