package com.oms.ui;

import java.util.Scanner;

import com.oms.exception.OMSException;
import com.oms.model.*;
import com.oms.repository.*;
import com.oms.service.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        ProductRepository productRepo = new ProductRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        OrderRepository orderRepo = new OrderRepository();
        InvoiceRepository invoiceRepo = new InvoiceRepository();
        InventoryRepository inventoryRepo = new InventoryRepository();

        InventoryService inventoryService = new InventoryService(inventoryRepo);
        OrderService orderService = new OrderService(inventoryService, orderRepo, invoiceRepo);
        InvoiceService invoiceService = new InvoiceService(invoiceRepo);

        // ----------------- SAMPLE DATA -----------------
        productRepo.addProduct(new Product("P101", "Laptop", 55000));
        productRepo.addProduct(new Product("P102", "Mouse", 500));
        productRepo.addProduct(new Product("P103", "Keyboard", 1500));

        inventoryRepo.addStock("P101", 10);
        inventoryRepo.addStock("P102", 50);
        inventoryRepo.addStock("P103", 20);

        customerRepo.addCustomer(new Customer(1, "Shubha", "shubha@gmail.com", "9876543210", "Bangalore"));

        int orderIdCounter = 1;

        while (true) {

            System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Online Order");
            System.out.println("2. Create Offline Order");
            System.out.println("3. Add Item to Order");
            System.out.println("4. Complete Order");
            System.out.println("5. Print Invoice");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            int ch = sc.nextInt();
            sc.nextLine();

            try {
                switch (ch) {

                    case 1:
                        System.out.println("Enter Customer ID: ");
                        int cid = sc.nextInt(); sc.nextLine();
                        Customer cust = customerRepo.getCustomer(cid);
                        if (cust == null) {
                            System.out.println("Customer not found!");
                            break;
                        }

                        System.out.print("Delivery Address: ");
                        String da = sc.nextLine();

                        System.out.print("Shipping Charge: ");
                        double ship = sc.nextDouble();

                        OnlineOrder on = new OnlineOrder(orderIdCounter++, cust, da, ship);
                        orderService.createOrder(on);
                        System.out.println("Online Order Created!");
                        break;

                    case 2:
                        System.out.println("Enter Customer ID: ");
                        int cid2 = sc.nextInt(); sc.nextLine();
                        Customer cust2 = customerRepo.getCustomer(cid2);
                        if (cust2 == null) {
                            System.out.println("Customer not found!");
                            break;
                        }

                        System.out.print("Store Location: ");
                        String sl = sc.nextLine();

                        OfflineOrder off = new OfflineOrder(orderIdCounter++, cust2, sl);
                        orderService.createOrder(off);
                        System.out.println("Offline Order Created!");
                        break;

                    case 3:
                        System.out.print("Order ID: ");
                        int oid = sc.nextInt(); sc.nextLine();

                        System.out.print("Product ID: ");
                        String pid = sc.nextLine();

                        Product p = productRepo.getProduct(pid);
                        if (p == null) {
                            System.out.println("Product not found!");
                            break;
                        }

                        System.out.print("Quantity: ");
                        int qty = sc.nextInt();

                        orderService.addItemToOrder(oid, new OrderItem(p, qty));
                        System.out.println("Item Added!");
                        break;

                    case 4:
                        System.out.print("Order ID: ");
                        int co = sc.nextInt();

                        Invoice inv = orderService.completeOrder(co);
                        System.out.println("Order Completed! Invoice ID: " + inv.getInvoiceId());
                        break;

                    case 5:
                        System.out.print("Invoice ID: ");
                        int iid = sc.nextInt();
                        invoiceService.printInvoice(iid);
                        break;

                    case 6:
                        System.out.println("Exiting...");
                        System.exit(0);
                }

            } catch (OMSException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
