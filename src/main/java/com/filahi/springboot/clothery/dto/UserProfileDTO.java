package com.filahi.springboot.clothery.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserProfileDTO {
    private String auth0Id;
    private String email;
    private String firstName;
    private String lastName;
}
