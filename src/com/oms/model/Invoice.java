package com.oms.model;

public class Invoice {

    private int invoiceId;
    private int orderId;
    private double totalAmount;

    // Customer details (always stored as simple data)
    private int customerId;
    private String customerName;
    private String phone;
    private String email;

    // Online order details
    private String deliveryAddress;
    private double shippingCharge;

    public Invoice(int invoiceId, int orderId, double totalAmount,
                   int customerId, String customerName, String phone, String email,
                   String deliveryAddress, double shippingCharge) {

        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.totalAmount = totalAmount;

        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;

        this.deliveryAddress = deliveryAddress;   // null for offline
        this.shippingCharge = shippingCharge;     // 0 for offline
    }

    public int getInvoiceId() { return invoiceId; }
    public int getOrderId() { return orderId; }
    public double getTotalAmount() { return totalAmount; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("============ INVOICE ============\n");
        sb.append("Invoice ID: ").append(invoiceId).append("\n");
        sb.append("Order ID: ").append(orderId).append("\n");

        sb.append("Customer ID: ").append(customerId).append("\n");
        sb.append("Name: ").append(customerName).append("\n");
        sb.append("Phone: ").append(phone).append("\n");
        sb.append("Email: ").append(email).append("\n");

        if (deliveryAddress != null) {
            sb.append("Delivery Address: ").append(deliveryAddress).append("\n");
            sb.append("Shipping Charge: Rs. ").append(shippingCharge).append("\n");
        }

        sb.append("Total Amount: Rs. ").append(totalAmount).append("\n");
        sb.append("=================================\n");

        return sb.toString();
    }
}
