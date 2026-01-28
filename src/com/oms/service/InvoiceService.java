package com.oms.service;

import com.oms.exception.NoInvoiceFoundException;
import com.oms.model.Invoice;
import com.oms.model.OrderItem;
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

    public void printInvoice(int invoiceId) {

        Invoice invoice = repo.getInvoice(invoiceId);
        if (invoice == null) {
            System.out.println("Invoice not found!");
            return;
        }

        System.out.println("\n========== INVOICE ==========");
        System.out.println("Invoice ID : " + invoice.getInvoiceId());
        System.out.println("Customer   : " + invoice.getCustomerName());
        System.out.println("Phone      : " + invoice.getPhone());

        // ONLINE order extras
        if (invoice.getDeliveryAddress() != null) {
            System.out.println("Delivery Address : " + invoice.getDeliveryAddress());
            System.out.println("Shipping Charge  : Rs." + invoice.getShippingCharge());
        }

        System.out.println("--------------------------------");
        System.out.println("Products:");
        System.out.println("--------------------------------");

        for (OrderItem item : invoice.getItems()) {
            System.out.println(
                item.getProduct().getName() + " x " +
                item.getQuantity() + " = Rs." +
                item.getSubtotal()
            );
        }

        System.out.println("--------------------------------");
        System.out.println("TOTAL AMOUNT : Rs." + invoice.getTotalAmount());
        System.out.println("================================");
    }
}
