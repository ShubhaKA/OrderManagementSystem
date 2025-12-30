
# 📦Order Management System (OMS)

A modular Order Management System built using Java, featuring clean architecture, repositories, services, models, and complete JUnit + Mockito test coverage. This project demonstrates end‑to‑end order processing including inventory checks, item management, invoice generation, and delivery tracking.


## 🚀 Features

🔹 Order Processing Create online & offline orders

- Add items to an order

- Calculate total with shipping

- Complete and fulfill orders

- Prevent duplicate invoice generation

🔹 Delivery Tracking (Online Orders) Update delivery status (PACKED → SHIPPED → DELIVERED)

- Maintain tracking log

- Auto-complete order on delivery

🔹 Inventory Management Check stock before adding items

- Reduce stock after order completion

🔹 Invoice Handling Generate invoice IDs

- Print invoices

- Fetch invoice by ID or order ID

🔹 Repository Pattern CustomerRepository

- OrderRepository

- ProductRepository

- InvoiceRepository

- InventoryRepository


## 🧩 Project Structure

src/ \
 ├── model/ \
 │    ├── Order.java \
 │    ├── OnlineOrder.java \
 │    ├── OfflineOrder.java \
 │    ├── OrderItem.java \
 │    ├── Customer.java \
 │    └── Invoice.java \
 │\
 ├── repository/ \
 │    ├── CustomerRepository.java \
 │    ├── OrderRepository.java \
 │    ├── ProductRepository.java \
 │    ├── InvoiceRepository.java \
 │    └── InventoryRepository.java \
 │\
 ├── service/ \
 │    ├── OrderService.java \
 │    ├── InvoiceService.java \
 │    ├── InventoryService.java \
 │\
 └── exception/ \
      ├── OMSException.java \
      ├── NoOrdersException.java \
      └── NoInvoiceFoundException.java 
## Technologies Used

- Java 17+

- JUnit 5

- Mockito

- Eclipse 
## Testing ✔ 100% Unit test coverage across:


- Service layer: OrderService, InvoiceService, InventoryService

- Repository layer: CRUD and ID generation

- Model layer: Order, OnlineOrder, OfflineOrder, OrderItem, Invoice

- End-to-end flow test (order → add items → complete order → invoice → delivery update)

Technologies used:

- JUnit 5

- Mockito

## How to Run Tests

Using IDE (Eclipse/IntelliJ):

Right‑click the test folder

Select Run All Tests

Using Maven:

```
  mvn test
```


## 📘 Sample Output (Invoice)


============ INVOICE ============ \
Invoice ID: 1001 \
Order ID: 10 \
Customer ID: 101 \
Name: Priya \
Phone: 9999999999 \
Email: priya@gmail.com \
Delivery Address: Bangalore \
Shipping Charge: Rs. 50 \
Total Amount: Rs. 100050 \
================================= 
