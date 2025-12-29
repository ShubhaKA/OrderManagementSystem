package com.oms.data;

import com.oms.model.Customer;
import com.oms.model.Product;
import com.oms.repository.CustomerRepository;
import com.oms.repository.InventoryRepository;
import com.oms.repository.ProductRepository;

import java.util.Map;

public class FileDataInitializer {

    public static void initializeData(
            ProductRepository productRepo,
            CustomerRepository customerRepo,
            InventoryRepository inventoryRepo
    ) {

        // Load Products
        Map<String, Product> products =
                FileDataLoader.loadProducts("data/products.txt");
        products.values().forEach(productRepo::addProduct);

        // Load Customers
        Map<Integer, Customer> customers =
                FileDataLoader.loadCustomers("data/customers.txt");
        customers.values().forEach(customerRepo::addCustomer);

        // Load Inventory
        Map<String, Integer> inventory =
                FileDataLoader.loadInventory("data/inventory.txt");
        inventory.forEach(inventoryRepo::setStock);

        System.out.println("Data loaded successfully from files");
    }
}
