package com.oms.exception;

public class NoInvoiceFoundException extends Exception {
   
	private static final long serialVersionUID = 1L;

	public NoInvoiceFoundException(String msg) {
        super(msg);
    }
}

