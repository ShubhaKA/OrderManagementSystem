package com.oms.repository;

import java.util.HashMap;
import java.util.Map;

public class InventoryRepository {

    // productId -> quantity
    private Map<String, Integer> stockMap = new HashMap<>();

    public void setStock(String productId, int quantity) {
        stockMap.put(productId, quantity);
    }

    public void addStock(String productId, int quantity) {
        int current = stockMap.getOrDefault(productId, 0);
        stockMap.put(productId, current + quantity);
    }

    public boolean hasStock(String productId, int requiredQty) {
        return stockMap.getOrDefault(productId, 0) >= requiredQty;
    }

    public boolean reduceStock(String productId, int qty) {
        int available = stockMap.getOrDefault(productId, 0);
        if (qty > available) {
            return false;
        }
        stockMap.put(productId, available - qty);
        return true;
    }

    public int getStock(String productId) {
        return stockMap.getOrDefault(productId, 0);
    }

    public Map<String, Integer> getAllStock() {
        return stockMap;
    }
}
