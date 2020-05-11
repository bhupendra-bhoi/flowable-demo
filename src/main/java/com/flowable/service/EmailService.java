package com.flowable.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

@Service
public class EmailService{
	
	public ConcurrentHashMap<String, AtomicInteger> sends = new ConcurrentHashMap<>();
	
	public void sendWelcomeEmail(String customerId, String email) {
		System.out.println("sending welcome email for "+ customerId + " to "+ email);
		sends.computeIfAbsent(email,  e-> new AtomicInteger());
		sends.get(email).incrementAndGet();
		
	}
}