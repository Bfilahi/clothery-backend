package com.filahi.springboot.clothery.service.impl;

import com.filahi.springboot.clothery.entity.Customer;
import com.filahi.springboot.clothery.repository.ICustomerRepository;
import com.filahi.springboot.clothery.service.IAuth0UserSyncService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class Auth0UserSyncServiceImpl implements IAuth0UserSyncService {
    private static final Logger LOGGER = LoggerFactory.getLogger(Auth0UserSyncServiceImpl.class);

    private final ICustomerRepository customerRepository;

    @Autowired
    public Auth0UserSyncServiceImpl(ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }


    @Override
    @Transactional
    public Customer syncUser(String auth0Id, String email, String firstName, String lastName) {
        LOGGER.info("Syncing user with Auth0 ID: {}", auth0Id);

        // Look for existing customer
        Customer customer = customerRepository.findByAuth0Id(auth0Id);

        if (customer == null) {
            LOGGER.info("Creating new customer record for Auth0 ID: {}", auth0Id);
            customer = new Customer();
            customer.setAuth0Id(auth0Id);
        } else {
            LOGGER.info("Found existing customer with ID: {} for Auth0 ID: {}", customer.getId(), auth0Id);
        }

        // Update customer details
        customer.setEmail(email);
        customer.setFirstName(firstName);
        customer.setLastName(lastName);

        // Save and return
        return customerRepository.save(customer);
    }
}
