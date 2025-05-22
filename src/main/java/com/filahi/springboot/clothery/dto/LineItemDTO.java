package com.filahi.springboot.clothery.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineItemDTO {
    private String name;
    private String description;
    private long amount;
    private long quantity;
}
