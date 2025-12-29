package com.oms.repository;

import java.util.HashMap;
import java.util.Map;

public class InventoryRepository {

    // productId -> quantity
    private Map<String, Integer> stockMap = new HashMap<>();

    // Used for file-based or bulk loading (IMPORTANT)
    public void setStock(String productId, int quantity) {
        stockMap.put(productId, quantity);
    }

    // Add stock (increase quantity)
    public void addStock(String productId, int quantity) {
        int current = stockMap.getOrDefault(productId, 0);
        stockMap.put(productId, current + quantity);
    }

    // Check if enough stock exists
    public boolean hasStock(String productId, int requiredQty) {
        return stockMap.getOrDefault(productId, 0) >= requiredQty;
    }

    // Reduce stock after order completion
    public void reduceStock(String productId, int quantity) {
        if (!hasStock(productId, quantity)) {
            throw new RuntimeException("Insufficient stock for product: " + productId);
        }
        stockMap.put(productId, stockMap.get(productId) - quantity);
    }

    // Get current stock
    public int getStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    // View full inventory
    public Map<String, Integer> getAllStock() {
        return stockMap;
    }
}
