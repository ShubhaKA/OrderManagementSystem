package com.oms.model;

public class Customer {

    private int customerId;
    private String name;
    private String email;
    private String phone;
    private String address;

    public Customer(int customerId, String name, String email, String phone, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public int getCustomerId() { return customerId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }

    @Override
    public String toString() {
        return "Customer ID: " + customerId +
                ", Name: " + name +
                ", Email: " + email +
                ", Phone: " + phone +
                ", Address: " + address;
    }
}
