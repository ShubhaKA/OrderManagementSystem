package com.oms.model;

import java.util.List;

public class Invoice {

    private int invoiceId;
    private int orderId;
    private double totalAmount;
    private int customerId;
    private String customerName;
    private String phone;
    private String email;
    private String deliveryAddress;
    private double shippingCharge;

    // ✅ NEW
    private List<OrderItem> items;

    public Invoice(int invoiceId, int orderId, double totalAmount,
                   int customerId, String customerName, String phone,
                   String email, String deliveryAddress,
                   double shippingCharge,
                   List<OrderItem> items) {

        this.invoiceId = invoiceId;
        this.orderId = orderId;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
        this.customerName = customerName;
        this.phone = phone;
        this.email = email;
        this.deliveryAddress = deliveryAddress;
        this.shippingCharge = shippingCharge;
        this.items = items;
    }
    
    public Invoice(int invoiceId, int orderId, double totalAmount,
            int customerId, String customerName, String phone,
            String email, String deliveryAddress,
            double shippingCharge) {

 this(invoiceId, orderId, totalAmount,
      customerId, customerName, phone,
      email, deliveryAddress, shippingCharge,
      null); // items not available
}

    public List<OrderItem> getItems() {
        return items;
    }

    public int getCustomerId() {
		return customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	public double getShippingCharge() {
		return shippingCharge;
	}

	public int getInvoiceId() { return invoiceId; }
    public int getOrderId() { return orderId; }
    public double getTotalAmount() { return totalAmount; }
    
    

    public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	public void setShippingCharge(double shippingCharge) {
		this.shippingCharge = shippingCharge;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

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
