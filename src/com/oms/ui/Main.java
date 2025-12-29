package com.oms.ui;

import java.util.Scanner;

import com.oms.data.FileDataInitializer;
import com.oms.exception.NoOrdersException;
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
        InventoryService inventoryService = new InventoryService(inventoryRepo);
        OrderService orderService = new OrderService(inventoryService, orderRepo, invoiceRepo);
        InvoiceService invoiceService = new InvoiceService(invoiceRepo);

        int choice;

        // ========= MAIN MENU ==========
        while (true) {
            System.out.println("\n===== ORDER MANAGEMENT SYSTEM =====");
            System.out.println("1. Create Online Order");
            System.out.println("2. Create Offline Order");
            System.out.println("3. Add Item to Order");
            System.out.println("4. Complete Order");
            System.out.println("5. Print Invoice");
            System.out.println("6. View All Products");
            System.out.println("7. View All Customers");
            System.out.println("8. View All Orders");
            System.out.println("9. Track Online Order");
            System.out.println("10. Update Delivery Status");
            System.out.println("11. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {

            // ---------------------------------------
            case 1: // ONLINE ORDER
                System.out.print("Enter Customer ID: ");
                int custId1 = sc.nextInt();
                Customer c1 = customerRepo.getCustomer(custId1);
                if (c1 == null) {
                    System.out.println("Customer not found!");
                    break;
                }
                sc.nextLine();
                System.out.print("Delivery Address: ");
                String address = sc.nextLine();
                System.out.print("Shipping Charge: ");
                double shipping = sc.nextDouble();

                int orderId1 = orderRepo.generateOrderId();
                Order onlineOrder = new OnlineOrder(orderId1, c1, address, shipping);
                orderService.createOrder(onlineOrder);

                System.out.println("Online Order Created! Order ID: " + orderId1);
                break;

            // ---------------------------------------
            case 2: // OFFLINE ORDER
                System.out.print("Enter Customer ID: ");
                int custId2 = sc.nextInt();
                Customer c2 = customerRepo.getCustomer(custId2);
                if (c2 == null) {
                    System.out.println("Customer not found!");
                    break;
                }
                sc.nextLine();
                System.out.print("Store Location: ");
                String store = sc.nextLine();

                int orderId2 = orderRepo.generateOrderId();
                Order offlineOrder = new OfflineOrder(orderId2, c2, store);
                orderService.createOrder(offlineOrder);

                System.out.println("Offline Order Created! Order ID: " + orderId2);
                break;

            // ---------------------------------------
            case 3: // ADD ITEM
                System.out.print("Order ID: ");
                int oid = sc.nextInt();
                sc.nextLine();

                System.out.print("Product ID: ");
                String pid = sc.nextLine();

                Product p = productRepo.getProduct(pid);
                if (p == null) {
                    System.out.println("Product not found!");
                    break;
                }

                System.out.print("Quantity: ");
                int qty = sc.nextInt();

                try {
                    orderService.addItemToOrder(oid, new OrderItem(p, qty));
                    System.out.println("Item Added!");
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;

            // ---------------------------------------
            case 4: // COMPLETE ORDER
                System.out.print("Order ID: ");
                int cid = sc.nextInt();
                try {
                    Invoice inv = orderService.completeOrder(cid);
                    System.out.println("Order Completed! Invoice ID: " + inv.getInvoiceId());
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;

            // ---------------------------------------
            case 5: // PRINT INVOICE
                System.out.print("Invoice ID: ");
                int invId = sc.nextInt();
                try {
                    invoiceService.printInvoice(invId);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;

            // ---------------------------------------
            case 6: // VIEW PRODUCTS
                System.out.println("===== AVAILABLE PRODUCTS =====");
                productRepo.getAllProducts().values().forEach(System.out::println);
                break;

            // ---------------------------------------
            case 7: // VIEW CUSTOMERS
                System.out.println("===== CUSTOMERS =====");
                customerRepo.getAllCustomers().values().forEach(System.out::println);
                break;

            // ---------------------------------------
            case 8: // VIEW ORDERS
                System.out.println("===== ALL ORDERS =====");
                try {

                    if (orderRepo.getAllOrders().isEmpty()) {
                        throw new NoOrdersException("No orders found!");
                    }

                    for (Order o : orderRepo.getAllOrders().values()) {
                        System.out.println(o);
                        System.out.println("Items:");
                        for (OrderItem item : o.getItems()) {
                            System.out.println(" - " + item);
                        }
                        System.out.println("--------------------------------");
                    }

                } catch (NoOrdersException e) {
                    System.out.println(e.getMessage());
                }
                break;

            // ---------------------------------------
            case 9: // TRACK ONLINE ORDER
                System.out.print("Enter Order ID: ");
                int oidTrack = sc.nextInt();

                Order ord = orderService.getOrder(oidTrack);

                if (ord instanceof OnlineOrder) {
                    System.out.println("---- ORDER TRACKING ----");
                    ord.getTrackingUpdates().forEach(System.out::println);
                } else {
                    System.out.println("Not an online order!");
                }
                break;

            // ---------------------------------------
            case 10: // UPDATE DELIVERY STATUS
                System.out.print("Enter Order ID: ");
                int oidStat = sc.nextInt();
                sc.nextLine();

                System.out.println("Enter new Status (PACKED / SHIPPED / OUT_FOR_DELIVERY / DELIVERED): ");
                String status = sc.nextLine();

                try {
                    orderService.updateDeliveryStatus(oidStat, status);
                    System.out.println("Delivery Status Updated!");
                } catch (OMSException e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;

            // ---------------------------------------
            case 11:
                System.out.println("Exiting...");
                return;

            default:
                System.out.println("Invalid choice!");
            }
        }
    }
}
