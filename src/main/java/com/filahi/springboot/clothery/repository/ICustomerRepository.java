package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByAuth0Id(String auth0Id);
}
