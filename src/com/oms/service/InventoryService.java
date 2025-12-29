package com.oms.service;

import com.oms.exception.OMSException;
import com.oms.repository.InventoryRepository;

public class InventoryService {

    private InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    public void checkStock(String productId, int qty) throws OMSException {
        if (repo.getStock(productId) < qty) {
            throw new OMSException("Insufficient stock!");
        }
    }

    public void reduceStock(String productId, int qty) {
        repo.reduceStock(productId, qty);
    }

    public void addStock(String productId, int qty) {
        repo.addStock(productId, qty);
    }
}
