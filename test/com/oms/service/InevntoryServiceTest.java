package com.oms.service;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oms.exception.OMSException;
import com.oms.repository.InventoryRepository;

class InventoryServiceTest {

    InventoryRepository repo;
    InventoryService service;

    @BeforeEach
    void setup() {
        repo = new InventoryRepository();
        service = new InventoryService(repo);

        // Preload stock
        repo.addStock("P1", 10);
    }

    @Test
    void testCheckStockSuccess() throws OMSException {
        assertDoesNotThrow(() -> service.checkStock("P1", 5));
    }

    @Test
    void testCheckStockFailure() {
        OMSException ex = assertThrows(
                OMSException.class,
                () -> service.checkStock("P1", 20)
        );
        assertEquals("Insufficient stock for product: P1", ex.getMessage());
    }

    @Test
    void testReduceStock() {
        service.reduceStock("P1", 3);
        assertEquals(7, repo.getStock("P1"));
    }
}
