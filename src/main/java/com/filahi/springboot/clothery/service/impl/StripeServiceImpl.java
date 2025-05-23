package com.filahi.springboot.clothery.service.impl;

import com.filahi.springboot.clothery.dto.LineItemDTO;
import com.filahi.springboot.clothery.dto.PaymentRequestDTO;
import com.filahi.springboot.clothery.dto.StripeResponseDTO;
import com.filahi.springboot.clothery.service.IStripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class StripeServiceImpl implements IStripeService {

    @Value("${stripe.api.key}")
    private String apiKey;

    @Value("${currency}")
    private String currency;

    @Override
    public StripeResponseDTO checkout(PaymentRequestDTO paymentRequestDTO) throws StripeException {

        Stripe.apiKey = this.apiKey;

        // Create session params builder
        SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:4200/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:4200/cancel")
                .setShippingAddressCollection(
                        SessionCreateParams.ShippingAddressCollection.builder()
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.IT)
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.US)
                                .addAllowedCountry(SessionCreateParams.ShippingAddressCollection.AllowedCountry.GB)
                                .build()
                );


        // Adding Auth0 ID to metadata if provided
        if (paymentRequestDTO.getMetadata() != null && paymentRequestDTO.getMetadata().containsKey("auth0Id")) {
            paramsBuilder.putMetadata("auth0Id", paymentRequestDTO.getMetadata().get("auth0Id"));
        }


        // Adding each line item to the session
        for (LineItemDTO item : paymentRequestDTO.getLineItems()) {
            SessionCreateParams.LineItem.PriceData.ProductData.Builder productDataBuilder =
                    SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName(item.getName())
                            .setDescription("Clothery purchase");

            SessionCreateParams.LineItem.PriceData priceData = SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency(this.currency)
                    .setUnitAmount(item.getAmount())
                    .setProductData(productDataBuilder.build())
                    .build();

            SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                    .setQuantity(item.getQuantity())
                    .setPriceData(priceData)
                    .build();

            paramsBuilder.addLineItem(lineItem);

        }

        SessionCreateParams params = paramsBuilder.build();

        Session session = Session.create(params);

        return StripeResponseDTO.builder()
                .status("SUCCESS")
                .message("Payment session created")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();
    }
}
