package com.shopping.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.user.entity.Customer;
import com.shopping.user.service.CustomerService;

@RestController
@RequestMapping("/user")

public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/register")
	public void registerCustomer(@RequestBody Customer customer){
		customerService.registerCustomer(customer);
	}
	
	@PostMapping("/login")
	public void loginCustomer(@RequestBody Customer customer){
		customerService.loginCustomer(customer);
	}
}
