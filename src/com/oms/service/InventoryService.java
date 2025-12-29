package com.oms.service;

import com.oms.exception.OMSException;
import com.oms.repository.InventoryRepository;

public class InventoryService {

    private InventoryRepository inventoryRepo;

    public InventoryService(InventoryRepository inventoryRepo) {
        this.inventoryRepo = inventoryRepo;
    }

    // Check if enough stock is available
    public void checkStock(String productId, int quantity) throws OMSException {
        int available = inventoryRepo.getStock(productId);
        if (available < quantity) {
            throw new OMSException(
                "Insufficient stock for product: " + productId
            );
        }
    }

    // Reduce stock after order confirmation
    public void reduceStock(String productId, int quantity) {
        inventoryRepo.reduceStock(productId, quantity);
    }
}
