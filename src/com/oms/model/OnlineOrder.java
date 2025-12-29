package com.oms.model;

public class OnlineOrder extends Order {

    private String deliveryAddress;
    private double shippingCharge;

    public OnlineOrder(int orderId, Customer customer, String deliveryAddress, double shippingCharge) {
        super(orderId, customer);
        this.deliveryAddress = deliveryAddress;
        this.shippingCharge = shippingCharge;
    }

    @Override
    public void calculateTotal() {
        super.calculateTotal();
        totalAmount += shippingCharge;
    }

    @Override
    public void fulfillOrder() {
        updateStatus("PACKED");
        updateStatus("SHIPPED");
        updateStatus("DELIVERED");
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Delivery: " + deliveryAddress +
                ", Shipping: Rs." + shippingCharge;
    }
}
