package com.oms.ui;

import java.util.Scanner;

import com.oms.data.FileDataInitializer;
import com.oms.exception.OMSException;
import com.oms.model.*;
import com.oms.repository.*;
import com.oms.service.*;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // ---------- Repositories ----------
        ProductRepository productRepo = new ProductRepository();
        CustomerRepository customerRepo = new CustomerRepository();
        InventoryRepository inventoryRepo = new InventoryRepository();
        OrderRepository orderRepo = new OrderRepository();
        InvoiceRepository invoiceRepo = new InvoiceRepository();

        // ---------- Load data from files ----------
        FileDataInitializer.initializeData(
                productRepo,
                customerRepo,
                inventoryRepo
        );

        // ---------- Services ----------
        InventoryService inventoryService =
                new InventoryService(inventoryRepo);

        OrderService orderService =
                new OrderService(inventoryService, orderRepo, invoiceRepo);

        InvoiceService invoiceService =
                new InvoiceService(invoiceRepo);

        int choice;

        do {
            System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Online Order");
            System.out.println("2. Create Offline Order");
            System.out.println("3. Add Item to Order");
            System.out.println("4. Complete Order");
            System.out.println("5. Print Invoice");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            try {
                switch (choice) {

                    case 1: {
                        System.out.print("Enter Order ID: ");
                        int orderId = sc.nextInt();

                        System.out.print("Enter Customer ID: ");
                        int custId = sc.nextInt();

                        sc.nextLine(); // clear buffer
                        System.out.print("Delivery Address: ");
                        String address = sc.nextLine();

                        System.out.print("Shipping Charge: ");
                        double ship = sc.nextDouble();

                        Customer c = customerRepo.getCustomer(custId);
                        if (c == null) {
                            System.out.println("Customer not found!");
                            break;
                        }

                        Order order = new OnlineOrder(orderId, c, address, ship);
                        orderService.createOrder(order);

                        System.out.println("Online Order Created!");
                        break;
                    }

                    case 2: {
                        System.out.print("Enter Order ID: ");
                        int orderId = sc.nextInt();

                        System.out.print("Enter Customer ID: ");
                        int custId = sc.nextInt();

                        sc.nextLine();
                        System.out.print("Store Location: ");
                        String store = sc.nextLine();

                        Customer c = customerRepo.getCustomer(custId);
                        if (c == null) {
                            System.out.println("Customer not found!");
                            break;
                        }

                        Order order = new OfflineOrder(orderId, c, store);
                        orderService.createOrder(order);

                        System.out.println("Offline Order Created!");
                        break;
                    }

                    case 3: {
                        System.out.print("Order ID: ");
                        int orderId = sc.nextInt();

                        System.out.print("Product ID: ");
                        String pid = sc.next();

                        System.out.print("Quantity: ");
                        int qty = sc.nextInt();

                        Product p = productRepo.getProduct(pid);
                        if (p == null) {
                            System.out.println("Product not found!");
                            break;
                        }

                        OrderItem item = new OrderItem(p, qty);
                        orderService.addItemToOrder(orderId, item);

                        System.out.println("Item Added!");
                        break;
                    }

                    case 4: {
                        System.out.print("Order ID: ");
                        int orderId = sc.nextInt();

                        Invoice inv = orderService.completeOrder(orderId);
                        System.out.println("Order Completed! Invoice ID: " + inv.getInvoiceId());
                        break;
                    }

                    case 5: {
                        System.out.print("Invoice ID: ");
                        int invId = sc.nextInt();

                        invoiceService.printInvoice(invId);
                        break;
                    }

                    case 6:
                        System.out.println("Thank you!");
                        break;

                    default:
                        System.out.println("Invalid choice!");
                }

            } catch (OMSException e) {
                System.out.println("Error: " + e.getMessage());
            }
        } while (choice != 6);

        sc.close();
    }
}
