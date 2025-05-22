package com.filahi.springboot.clothery.dto;


import com.filahi.springboot.clothery.entity.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddressDTO {
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String postalCode;
    private String country;

    public AddressDTO(Address address) {
        this.line1 = address.getLine1();
        this.line2 = address.getLine2();
        this.city = address.getCity();
        this.state = address.getState();
        this.postalCode = address.getPostalCode();
        this.country = address.getCountry();
    }
}
