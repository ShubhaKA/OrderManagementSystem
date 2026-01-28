package com.oms.ui;

import java.util.ArrayList;
import java.util.List;
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
            System.out.println("1. Create Order");
            System.out.println("2. Print Invoice");
            System.out.println("3. View All Products");
            System.out.println("4. View All Customers");
            System.out.println("5. View All Orders");
            System.out.println("6. Track Online Order");
            System.out.println("7. Update Delivery Status");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");

            choice = sc.nextInt();

            switch (choice) {

            case 1: // CREATE ORDER
            	System.out.println("Select Type of Order");
                System.out.println("1. Online Order");
                System.out.println("2. Offline Order");
                int type = sc.nextInt();
                sc.nextLine(); 

                System.out.print("Enter Customer ID: ");
                int custId = sc.nextInt();
                sc.nextLine(); 

                Customer customer = customerRepo.getCustomer(custId);
                if (customer == null) {
                    System.out.println("Customer not found!");
                    break;
                }

                int orderId = orderRepo.generateOrderId();
                Order order;

                if (type == 1) {
                    System.out.print("Delivery Address: ");
                    String address = sc.nextLine();

                    System.out.print("Shipping Charge: ");
                    double shipping = sc.nextDouble();
                    sc.nextLine(); 

                    order = new OnlineOrder(orderId, customer, address, shipping);
                } else {
                    System.out.print("Store Location: ");
                    String store = sc.nextLine();
                    order = new OfflineOrder(orderId, customer, store);
                }

                List<OrderItem> items = new ArrayList<>();

                while (true) {

                    System.out.print("Product ID (or 'done'): ");
                    String pid = sc.nextLine().trim();  

                    if (pid.equalsIgnoreCase("done")) {
                        break;
                    }

                    Product product = productRepo.getProduct(pid);
                    if (product == null) {
                        System.out.println("Invalid product!");
                        continue;
                    }

                    int availableQty = inventoryService.getStock(pid);
                    //System.out.println("Available Quantity: " + availableQty);

                    if (availableQty == 0) {
                        System.out.println("Product is OUT OF STOCK. Choose another product.");
                        continue;
                    }

                    System.out.print("Enter Quantity: ");
                    int qty = sc.nextInt();
                    sc.nextLine(); // ✅ consume newline

                    if (qty <= 0) {
                        System.out.println("Quantity must be greater than zero!");
                        continue;
                    }

                    if (qty > availableQty) {
                        System.out.println(
                            "Insufficient stock! Available quantity is: " + availableQty
                        );
                        continue;
                    }

                    // ✅ Reduce stock immediately (REAL-TIME update)
                    inventoryService.reduceStock(pid, qty);

                    items.add(new OrderItem(product, qty));
                    System.out.println("Item added successfully.");
                }

                try {
                    Invoice invoice = orderService.createOrderWithItems(order, items);

                    System.out.println("Order Created! Order ID: " + orderId);

                    if (invoice != null) {
                        System.out.println("Order Completed! Invoice ID: " + invoice.getInvoiceId());
                    } else {
                        System.out.println("Online Order Created. Track delivery status.");
                    }

                } catch (OMSException e) {
                    System.out.println("❌ " + e.getMessage());
                    System.out.println("Please re-create the order with available products.");
                }

                break;


            // ---------------------------------------
            case 2: // PRINT INVOICE
                System.out.print("Invoice ID: ");
                int invId = sc.nextInt();

                try {
                    invoiceService.printInvoice(invId);
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                }
                break;

            // ---------------------------------------
            case 3: // VIEW PRODUCTS
                System.out.println("===== AVAILABLE PRODUCTS =====");
                productRepo.getAllProducts().values().forEach(System.out::println);
                break;

            // ---------------------------------------
            case 4: // VIEW CUSTOMERS
                System.out.println("===== CUSTOMERS =====");
                customerRepo.getAllCustomers().values().forEach(System.out::println);
                break;

            // ---------------------------------------
            case 5: // VIEW ORDERS
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
            case 6: // TRACK ONLINE ORDER
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
            case 7: // UPDATE DELIVERY STATUS
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
            case 8:
                System.out.println("Exiting...");
                return;

            default:
                System.out.println("Invalid choice!");
            }
        }
    }
}
