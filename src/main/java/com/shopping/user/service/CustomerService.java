package com.shopping.user.service;

import java.util.Optional;
import java.util.regex.Pattern;

import com.shopping.user.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shopping.user.entity.Customer;
import com.shopping.user.exception.CustomerException;
import com.shopping.user.repository.CustomerRepository;


@Service

public class CustomerService {

	@Autowired
	CustomerRepository customerRepository;

	@Autowired
	private JwtUtil jwtUtil;
	
	public ResponseEntity<String> registerCustomer(Customer customer) {
		String mailString = customer.getMail();
		String passString = customer.getPassword();
		if(passString.isEmpty()) {
			throw new CustomerException("Password cannot be empty, please enter a password");
		}
		else {
			if(validateMail(mailString)) {
				if(validateUser(mailString))
				{
					customerRepository.save(customer);
					return new ResponseEntity<String>("Successfully Registered", HttpStatus.CREATED);
				}
				else {
					throw new CustomerException("Please choose another mail ID");
				}
			}
			else {
				throw new CustomerException("Mail format is invalid");
			}
		}	
	}
	
	public ResponseEntity<String> loginCustomer(Customer customer) {
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
					return generateToken(existCustomer);
				}
				else {
					throw new CustomerException("Password incorrect, try again");
				}
			}
			else {
				throw new CustomerException("Please register as new user");
			}
		}
		else {
			throw new CustomerException("Mail format is invalid");
		}
		//return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
	}

	private ResponseEntity<String> generateToken(Customer existCustomer) {
		String token = jwtUtil.generateToken(existCustomer.getMail());

		return new ResponseEntity<String>(token, HttpStatus.CREATED);
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
			throw new CustomerException("Mail ID is already registered");
		}
	}
	
	public boolean userExist(String mail) {
		Optional<Customer> customer = customerRepository.findById(mail);
		if(customer.isPresent()) {  
			return true;
		}
		else {
			throw new CustomerException("Mail ID is not registered");
		}
	}

}
