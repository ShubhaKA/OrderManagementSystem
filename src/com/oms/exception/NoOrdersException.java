package com.oms.exception;

public class NoOrdersException extends Exception {
   
	private static final long serialVersionUID = 1L;

	public NoOrdersException(String message) {
        super(message);
    }
}
