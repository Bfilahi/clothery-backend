package com.filahi.springboot.clothery.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeQuantityDTO {
    public String size;
    public int quantity;

    public SizeQuantityDTO(){}

    public SizeQuantityDTO(@JsonProperty("size") String size, @JsonProperty("quantity") int quantity){
        this.size = size;
        this.quantity = quantity;
    }
}
