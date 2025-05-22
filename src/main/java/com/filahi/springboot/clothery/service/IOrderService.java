package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.entity.Address;

public interface IOrderService {
    public void saveShippingAddress(String sessionId, Address address);
}
