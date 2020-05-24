package com.example.rest;

public class OrderNotFoundException extends RuntimeException{

	OrderNotFoundException(Long id) {
		super("Order not Found " + id + "\n");
	}
}
