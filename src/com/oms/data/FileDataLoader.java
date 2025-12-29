package com.oms.data;

import com.oms.model.Customer;
import com.oms.model.Product;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

public class FileDataLoader {

    // Load products from products.txt
    public static Map<String, Product> loadProducts(String filePath) {
        Map<String, Product> products = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Product product = new Product(
                        data[0].trim(),
                        data[1].trim(),
                        Double.parseDouble(data[2].trim())
                );
                products.put(product.getProductId(), product);
            }

        } catch (Exception e) {
            System.out.println("Error loading products: " + e.getMessage());
        }

        return products;
    }

    // Load customers from customers.txt
    public static Map<Integer, Customer> loadCustomers(String filePath) {
        Map<Integer, Customer> customers = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                Customer customer = new Customer(
                        Integer.parseInt(data[0].trim()),
                        data[1].trim(),
                        data[2].trim(),
                        data[4].trim(),   // phone
                        data[3].trim()    // address
                );
                customers.put(customer.getCustomerId(), customer);
            }

        } catch (Exception e) {
            System.out.println("Error loading customers: " + e.getMessage());
        }

        return customers;
    }

    // Load inventory from inventory.txt
    public static Map<String, Integer> loadInventory(String filePath) {
        Map<String, Integer> inventory = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                inventory.put(data[0].trim(), Integer.parseInt(data[1].trim()));
            }

        } catch (Exception e) {
            System.out.println("Error loading inventory: " + e.getMessage());
        }

        return inventory;
    }
}
