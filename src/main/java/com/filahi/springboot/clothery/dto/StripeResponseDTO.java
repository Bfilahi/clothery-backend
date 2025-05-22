package com.filahi.springboot.clothery.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StripeResponseDTO {
    private String status;
    private String message;
    private String sessionId;
    private String sessionUrl;
}
