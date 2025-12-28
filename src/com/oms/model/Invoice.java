package com.oms.model;

import java.util.Date;

public class Invoice {

    private int invoiceId;
    private int orderId;
    private double amount;
    private Date invoiceDate;

    public Invoice(int invoiceId, int orderId, double amount) {
        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.amount = amount;
        this.invoiceDate = new Date();
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public int getOrderId() {
        return orderId;
    }

    public double getAmount() {
        return amount;
    }

    public Date getInvoiceDate() {
        return invoiceDate;
    }

    @Override
    public String toString() {
        return "Invoice ID: " + invoiceId +
               ", Order ID: " + orderId +
               ", Amount: Rs." + amount +
               ", Date: " + invoiceDate;
    }
}
