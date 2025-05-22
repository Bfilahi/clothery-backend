package com.filahi.springboot.clothery.service;


import com.filahi.springboot.clothery.entity.Customer;

public interface IAuth0UserSyncService {
    Customer syncUser(String auth0Id, String email, String firstName, String lastName);
}
