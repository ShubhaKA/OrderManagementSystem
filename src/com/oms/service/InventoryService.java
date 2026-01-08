package com.oms.service;

import com.oms.repository.InventoryRepository;

public class InventoryService {

    private InventoryRepository inventoryRepo;

    public InventoryService(InventoryRepository repo) {
        this.inventoryRepo = repo;
    }

 // Check if product exists and has stock
    public boolean hasStock(String productId, int qty) {
        return inventoryRepo.hasStock(productId, qty);
    }

    // REDUCE STOCK AFTER ORDER COMPLETION
    public void reduceStock(String productId, int qty) {
        inventoryRepo.reduceStock(productId, qty);
    }

 // Get available quantity
    public int getStock(String productId) {
        return inventoryRepo.getStock(productId);
    }

    // View full inventory
    public void printInventory() {
        System.out.println("===== INVENTORY =====");
        inventoryRepo.getAllStock().forEach((id, qty) ->
                System.out.println("Product: " + id + " | Qty: " + qty));
    }
}
