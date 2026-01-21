package com.filahi.springboot.clothery.service.impl;

import com.filahi.springboot.clothery.entity.Customer;
import com.filahi.springboot.clothery.repository.CustomerRepository;
import com.filahi.springboot.clothery.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer createCustomer(String auth0Id, String email, String firstName, String lastName) {
        Customer customer = new Customer();
        customer.setAuth0Id(auth0Id);
        customer.setEmail(email);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);

        return this.customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerByAuth0Id(String auth0Id) {
        return this.customerRepository.findByAuth0Id(auth0Id);
    }

    @Override
    public Customer findByAuth0Id(String auth0Id) {
        if(this.customerRepository.findByAuth0Id(auth0Id) != null)
            return this.customerRepository.findByAuth0Id(auth0Id);
        return null;
    }
}
