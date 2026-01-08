package com.oms.model;

public class OfflineOrder extends Order {

    private String storeLocation;

    public OfflineOrder(int orderId, Customer customer, String storeLocation) {
        super(orderId, customer);
        this.storeLocation = storeLocation;
    }

    @Override
    public void fulfillOrder() {
        setCompleted(true);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", Store Location: " + storeLocation;
    }
}
