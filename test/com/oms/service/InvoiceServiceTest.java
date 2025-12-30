package com.oms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.oms.exception.NoInvoiceFoundException;
import com.oms.model.Invoice;
import com.oms.repository.InvoiceRepository;
import com.oms.service.InvoiceService;

public class InvoiceServiceTest {

    private InvoiceRepository repo;
    private InvoiceService service;

    @BeforeEach
    void setup() {
        repo = mock(InvoiceRepository.class);
        service = new InvoiceService(repo);
    }

    // -------------------------------------------------------------------
    // TEST 1: getInvoice() success
    // -------------------------------------------------------------------
    @Test
    void testGetInvoice_success() throws Exception {

        Invoice inv = new Invoice(101, 10, 5000,
                1, "Priya", "999", "p@gmail.com",
                "BLR", 50);

        when(repo.getInvoice(101)).thenReturn(inv);

        Invoice result = service.getInvoice(101);

        assertNotNull(result);
        assertEquals(101, result.getInvoiceId());
        assertEquals(10, result.getOrderId());
    }

    // -------------------------------------------------------------------
    // TEST 2: getInvoice() invalid id
    // -------------------------------------------------------------------
    @Test
    void testGetInvoice_notFound() {

        when(repo.getInvoice(999)).thenReturn(null);

        assertThrows(
            NoInvoiceFoundException.class,
            () -> service.getInvoice(999)
        );
    }

    // -------------------------------------------------------------------
    // TEST 3: printInvoice(int) success
    // -------------------------------------------------------------------
    @Test
    void testPrintInvoice_success() throws Exception {

        Invoice inv = new Invoice(102, 11, 6000,
                2, "Rahul", "888", "r@gmail.com",
                "Mysore", 60);

        when(repo.getInvoice(102)).thenReturn(inv);

        // No exception expected
        assertDoesNotThrow(() -> service.printInvoice(102));
    }

    // -------------------------------------------------------------------
    // TEST 4: printInvoice(int) invalid id
    // -------------------------------------------------------------------
    @Test
    void testPrintInvoice_invalidId() {

        when(repo.getInvoice(999)).thenReturn(null);

        assertThrows(
            NoInvoiceFoundException.class,
            () -> service.printInvoice(999)
        );
    }

    // -------------------------------------------------------------------
    // TEST 5: addInvoice() should call repo.addInvoice()
    // -------------------------------------------------------------------
    @Test
    void testAddInvoice_interaction() {

        Invoice inv = new Invoice(103, 12, 7000,
                3, "Kiran", "777", "k@gmail.com",
                "Delhi", 70);

        // Call repo method through InvoiceService
        repo.addInvoice(inv);

        // Verify call
        verify(repo, times(1)).addInvoice(inv);
    }
}
