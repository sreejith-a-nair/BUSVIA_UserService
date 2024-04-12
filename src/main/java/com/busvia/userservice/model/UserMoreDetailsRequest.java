package com.busvia.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMoreDetailsRequest {
    private String userId;
    private Date dob;
    private String additionalContact;
    private String zipCode;
    private String district;
    private String state;
    private String city;

}
