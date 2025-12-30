package com.oms.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.oms.model.Product;
import com.oms.repository.ProductRepository;

class ProductRepositoryTest {

    @Test
    void testAddAndGetProduct() {
        ProductRepository repo = new ProductRepository();

        Product p = new Product("P1", "Laptop", 60000);
        repo.addProduct(p);

        Product result = repo.getProduct("P1");

        assertNotNull(result);
        assertEquals("Laptop", result.getName());
    }

    @Test
    void testGetAllProducts() {
        ProductRepository repo = new ProductRepository();

        repo.addProduct(new Product("P1", "Laptop", 60000));
        repo.addProduct(new Product("P2", "Mobile", 20000));

        assertEquals(2, repo.getAllProducts().size());
    }
}
