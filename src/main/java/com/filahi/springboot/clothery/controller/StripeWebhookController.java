package com.filahi.springboot.clothery.controller;

import com.filahi.springboot.clothery.entity.*;
import com.filahi.springboot.clothery.repository.ICustomerRepository;
import com.filahi.springboot.clothery.repository.IProductRepository;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.LineItem;
import com.stripe.model.LineItemCollection;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionRetrieveParams;
import jakarta.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/webhook")
public class StripeWebhookController {

    private final IProductRepository productRepository;
    private final ICustomerRepository customerRepository;


    @Value("${stripe.webhook.secret}")
    private String stripeWebhookSecret;

    @Autowired
    public StripeWebhookController(IProductRepository productRepository,
                                   ICustomerRepository customerRepository){

        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }



    @PostMapping("/stripe")
    @Transactional
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);

            // Handle the checkout.session.completed event
            if ("checkout.session.completed".equals(event.getType())) {
                EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();

                if (dataObjectDeserializer.getObject().isPresent()) {
                    Session session = (Session) dataObjectDeserializer.getObject().get();

                    // Extract the shipping address from the session
                    if (session.getShippingDetails() != null && session.getShippingDetails().getAddress() != null) {
                        Address address = getAddress(session);

                        // Extract order details from the session
                        processCompletedCheckout(session, address);
                    } else {
                        return new ResponseEntity<>("Session missing shipping details", HttpStatus.BAD_REQUEST);
                    }
                } else
                    return new ResponseEntity<>("Unable to deserialize event data", HttpStatus.BAD_REQUEST);
            }

            return new ResponseEntity<>("Webhook received successfully", HttpStatus.OK);
        } catch (NoResultException e) {
            return new ResponseEntity<>("Data not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (StripeException e) {
            return new ResponseEntity<>("Stripe error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Webhook error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private static Address getAddress(Session session) {
        com.stripe.model.Address stripeAddress = session.getShippingDetails().getAddress();

        // Create Address object
        Address address = new Address();
        address.setLine1(stripeAddress.getLine1());
        address.setLine2(stripeAddress.getLine2());
        address.setCity(stripeAddress.getCity());
        address.setState(stripeAddress.getState());
        address.setPostalCode(stripeAddress.getPostalCode());
        address.setCountry(stripeAddress.getCountry());
        return address;
    }

    @Transactional
    private void processCompletedCheckout(Session session, Address address) throws StripeException {

        // Retrieve the expanded session with line items
        SessionRetrieveParams params = SessionRetrieveParams.builder()
                .addExpand("line_items")
                .build();
        Session expandedSession = Session.retrieve(session.getId(), params, null);

        // Get the line items
        LineItemCollection lineItems = expandedSession.getLineItems();


        String auth0Id = expandedSession.getMetadata().get("auth0Id");
        if (auth0Id == null || auth0Id.isEmpty())
            throw new NoResultException("Auth0Id is missing in session metadata");

        // Find customer by Auth0Id
        Customer customer = findCustomerByAuth0Id(auth0Id);


//        // Find customer by Auth0Id
//        Customer customer = this.customerRepository.findByAuth0Id(auth0Id);
//        if (customer == null) {
//
//            // Try finding by removing any pipe characters or other formatting
//            String simplifiedAuth0Id = auth0Id.replace("|", "");
//            customer = this.customerRepository.findByAuth0Id(simplifiedAuth0Id);
//
//            if (customer == null) {
//                // Try with just the ID portion after the pipe
//                if (auth0Id.contains("|")) {
//                    String idPortion = auth0Id.split("\\|")[1];
//                    customer = this.customerRepository.findByAuth0Id(idPortion);
//                }
//            }
//
//            if (customer == null)
//                throw new NoResultException("Customer not found with Auth0 ID: " + auth0Id);
//        }

        Order order = new Order();
        order.setStripeSessionId(session.getId());
        order.setShippingAddress(address);
        order.setOrderTrackingNumber(generateOrderTrackingNumber());

        customer.add(order);

        // Calculate totals
        int totalQuantity = 0;
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Process each line item
        for (LineItem item : lineItems.getData()) {
            String productName = item.getDescription();
            Long quantity = item.getQuantity();

            BigDecimal unitAmount = BigDecimal.valueOf(item.getPrice().getUnitAmount());

            // Calculate total amount for this item
            BigDecimal totalAmount = unitAmount.multiply(new BigDecimal(quantity));


            OrderItem orderItem = new OrderItem();
            orderItem.setName(productName);
            orderItem.setUnitPrice(unitAmount.divide(new BigDecimal(100)));
            orderItem.setQuantity(quantity);

            orderItem.setOrder(order);


            Optional<Product> product = this.productRepository.findByProductName(productName);

            if(product.isPresent()){
                Product theProduct = product.get();
                orderItem.setProduct(theProduct);

                if(theProduct.getImages() != null && !theProduct.getImages().isEmpty())
                    orderItem.setImageUrl(theProduct.getImages().get(0).getImgUrl());

            }
            else
                throw new NoResultException("Product not found with name: " + productName);

            order.add(orderItem);

            // Calculate totals
            totalQuantity += quantity;
            totalPrice = totalPrice.add(totalAmount);
        }

        order.setTotalQuantity(totalQuantity);
        order.setTotalPrice(totalPrice.divide(new BigDecimal(100)));

        this.customerRepository.save(customer);
    }


    private Customer findCustomerByAuth0Id(String auth0Id) {
        Customer customer = this.customerRepository.findByAuth0Id(auth0Id);
        if (customer == null) {
            // Try finding by removing any pipe characters or other formatting
            String simplifiedAuth0Id = auth0Id.replace("|", "");
            customer = this.customerRepository.findByAuth0Id(simplifiedAuth0Id);

            if (customer == null) {
                // Try with just the ID portion after the pipe
                if (auth0Id.contains("|")) {
                    String idPortion = auth0Id.split("\\|")[1];
                    customer = this.customerRepository.findByAuth0Id(idPortion);
                }
            }

            if (customer == null)
                throw new NoResultException("Customer not found with Auth0 ID: " + auth0Id);
        }
        return customer;
    }


    public static String generateOrderTrackingNumber(){
        return UUID.randomUUID().toString();
    }
}
