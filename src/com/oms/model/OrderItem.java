package com.oms.model;

public class OrderItem {

    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderItem() {
		// TODO Auto-generated constructor stub
	}

	public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }

    public void setQuantity(int qty) {
        this.quantity = qty;
    }

    public double getSubtotal() {
        return product.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return product.getName() + " x " + quantity + " = Rs." + getSubtotal();
    }
}
