package com.oms.model;

public class Invoice {

    private int invoiceId;
    private int orderId;
    private double totalAmount;

    public Invoice(int invoiceId, int orderId, double totalAmount) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
    }

    public int getInvoiceId() { return invoiceId; }
    public int getOrderId() { return orderId; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        return "Invoice ID: " + invoiceId +
                "\nOrder ID: " + orderId +
                "\nTotal Amount: Rs." + totalAmount;
    }
}
