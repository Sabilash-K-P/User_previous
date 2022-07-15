package com.shopping.user.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopping.user.entity.Customer;
import com.shopping.user.exception.CustomerException;
import com.shopping.user.repository.CustomerRepository;


@Service

public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;
	
	public void registerCustomer(Customer customer) {
		String mailString = customer.getMail();
		//String passString = customer.getPassword();
		if(validateMail(mailString)) {
			if(validateUser(mailString))
			{
				customerRepository.save(customer);
			}
		}
		else {
			throw new CustomerException("Mail format is invalid");
		}	
	}
	
	public void loginCustomer(Customer customer) {
		String mailString = customer.getMail();
		String passString = customer.getPassword();
		if(validateMail(mailString)) {
			if(userExist(mailString))
			{
				// Login functionality
				Optional<Customer> existingUser = customerRepository.findById(mailString);
				Customer existCustomer = existingUser.get();
				String savedPassString = existCustomer.getPassword();
				if(passString.equals(savedPassString)) {
					// Login
				}
				else {
					throw new CustomerException("Password incorrect, try again");
				}
			}
		}
		else {
			throw new CustomerException("Mail format is invalid");
		}	
	}
	
	public static boolean validateMail(String emailAddress) {
		String regexPattern = "^(.+)@(\\S+)$";
	    return Pattern.compile(regexPattern)
	      .matcher(emailAddress)
	      .matches();
	}
	
	public boolean validateUser(String mail) {
		Optional<Customer> customer = customerRepository.findById(mail);
		if(customer.isEmpty()) {  
			return true;
		}
		else {
			throw new CustomerException("Mail ID is already registered, please choose another mail ID");
		}
	}
	
	public boolean userExist(String mail) {
		Optional<Customer> customer = customerRepository.findById(mail);
		if(customer.isPresent()) {  
			return true;
		}
		else {
			throw new CustomerException("Mail ID is not registered, please register as new user");
		}
	}

}
