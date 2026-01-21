package com.filahi.springboot.clothery.controller;

import com.filahi.springboot.clothery.dto.PaymentRequestDTO;
import com.filahi.springboot.clothery.dto.StripeResponseDTO;
import com.filahi.springboot.clothery.service.StripeService;
import com.stripe.exception.StripeException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
public class PaymentController {

    private final StripeService stripeService;

    @Autowired
    public PaymentController(StripeService stripeService){
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponseDTO> createPaymentIntent(@RequestBody PaymentRequestDTO paymentRequest) throws StripeException {

        StripeResponseDTO stripeResponse = this.stripeService.checkout(paymentRequest);

        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);
    }
}
