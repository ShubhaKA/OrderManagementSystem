package com.oms.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.oms.model.Invoice;
import com.oms.repository.InvoiceRepository;

class InvoiceRepositoryTest {

    @Test
    void testGenerateInvoiceId() {
        InvoiceRepository repo = new InvoiceRepository();

        int id1 = repo.generateInvoiceId();
        int id2 = repo.generateInvoiceId();

        assertEquals(id1 + 1, id2);
    }

    @Test
    void testAddAndGetInvoice() {
        InvoiceRepository repo = new InvoiceRepository();

        Invoice inv = new Invoice(1001, 10, 5000, 101, "Priya", "999", "p@gmail.com", "BLR", 50);
        repo.addInvoice(inv);

        Invoice fetched = repo.getInvoice(1001);

        assertNotNull(fetched);
        assertEquals(10, fetched.getOrderId());
    }

    @Test
    void testGetInvoiceByOrderId() {
        InvoiceRepository repo = new InvoiceRepository();

        Invoice inv = new Invoice(2001, 50, 8000, 102, "Rahul", "888", "r@gmail.com", "MYS", 30);
        repo.addInvoice(inv);

        Invoice result = repo.getInvoiceByOrderId(50);

        assertNotNull(result);
        assertEquals(2001, result.getInvoiceId());
    }
}
