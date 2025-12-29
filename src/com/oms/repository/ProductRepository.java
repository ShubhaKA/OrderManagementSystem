package com.oms.repository;

import java.util.HashMap;
import java.util.Map;
import com.oms.model.Product;

public class ProductRepository {

    private Map<String, Product> products = new HashMap<>();

    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }

    public Product getProduct(String id) {
        return products.get(id);
    }

    public Map<String, Product> getAllProducts() {
        return products;
    }
}
