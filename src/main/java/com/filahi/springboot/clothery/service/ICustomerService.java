package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.entity.Customer;

public interface ICustomerService {
    Customer createCustomer(String auth0Id, String email, String firstName, String lastName);
    Customer getCustomerByAuth0Id(String auth0Id);
    Customer findByAuth0Id(String auth0Id);
}