package com.oms.service;

import com.oms.exception.NoInvoiceFoundException;
import com.oms.model.Invoice;
import com.oms.repository.InvoiceRepository;

public class InvoiceService {

    private InvoiceRepository repo;

    public InvoiceService(InvoiceRepository repo) {
        this.repo = repo;
    }

    public Invoice getInvoice(int invoiceId) throws NoInvoiceFoundException {
        Invoice invoice = repo.getInvoice(invoiceId);

        if (invoice == null) {
            throw new NoInvoiceFoundException("No Invoice Found!");
        }
        return invoice;
    }

    public void printInvoice(Invoice invoice) {
        System.out.println(invoice.toString());
    }
    
    public void printInvoice(int invoiceId) throws NoInvoiceFoundException {
        Invoice invoice = repo.getInvoice(invoiceId);
        if (invoice == null) {
            throw new NoInvoiceFoundException("Invoice not found!");
        }
        printInvoice(invoice); 
    }
}
