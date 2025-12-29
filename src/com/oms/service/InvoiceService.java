package com.oms.service;

import com.oms.model.Invoice;
import com.oms.repository.InvoiceRepository;

public class InvoiceService {

    private InvoiceRepository repo;

    public InvoiceService(InvoiceRepository repo) {
        this.repo = repo;
    }

    public void printInvoice(int id) {
        Invoice invoice = repo.getInvoice(id);
        if (invoice == null) {
            System.out.println("Invoice not found!");
            return;
        }
        System.out.println("========== INVOICE ==========");
        System.out.println(invoice);
        System.out.println("=============================");
    }
}
