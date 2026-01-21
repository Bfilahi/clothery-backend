package com.filahi.springboot.clothery.repository;

import com.filahi.springboot.clothery.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByAuth0Id(String auth0Id);
}
