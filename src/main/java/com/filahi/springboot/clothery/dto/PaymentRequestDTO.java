package com.filahi.springboot.clothery.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PaymentRequestDTO {
    private List<LineItemDTO> lineItems;
    private Map<String, String> metadata;
}
