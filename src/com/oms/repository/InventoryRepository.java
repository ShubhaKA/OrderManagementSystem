package com.oms.repository;

import java.util.HashMap;
import java.util.Map;

public class InventoryRepository {

    private Map<String, Integer> stock = new HashMap<>();

    public void addStock(String productId, int quantity) {
        stock.put(productId, quantity);
    }

    public int getStock(String productId) {
        return stock.getOrDefault(productId, 0);
    }

    public void reduceStock(String productId, int quantity) {
        stock.put(productId, getStock(productId) - quantity);
    }
}
