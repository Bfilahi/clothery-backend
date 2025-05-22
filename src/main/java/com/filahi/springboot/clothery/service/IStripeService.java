package com.filahi.springboot.clothery.service;

import com.filahi.springboot.clothery.dto.PaymentRequestDTO;
import com.filahi.springboot.clothery.dto.StripeResponseDTO;
import com.stripe.exception.StripeException;

public interface IStripeService {
    StripeResponseDTO checkout(PaymentRequestDTO paymentRequestDTO) throws StripeException;
}
