package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.entity.Address;

public interface OrderService {
    public void saveShippingAddress(String sessionId, Address address);
}
