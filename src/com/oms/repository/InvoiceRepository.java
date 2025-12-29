package com.oms.repository;

import java.util.HashMap;
import java.util.Map;
import com.oms.model.Invoice;

public class InvoiceRepository {

    private Map<Integer, Invoice> invoices = new HashMap<>();
    private int currentId = 1000;

    public int generateInvoiceId() {
        return currentId++;
    }

    public void addInvoice(Invoice invoice) {
        invoices.put(invoice.getInvoiceId(), invoice);
    }

    public Invoice getInvoice(int id) {
        return invoices.get(id);
    }

    public Invoice getInvoiceByOrderId(int orderId) {
        for (Invoice inv : invoices.values()) {
            if (inv.getOrderId() == orderId) {
                return inv;
            }
        }
        return null;
    }
}
