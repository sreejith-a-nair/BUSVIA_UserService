package com.busvia.userservice.model;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class UserProfileResponse {

    private UUID uuid;
    private  String firstName;
    private  String lastName;
    private String email;
    private String contact;
    private String password;
    private  String role;
    private String gender;
    private boolean isBlock;
    private Date dob;
    private String additionalContact;
    private String zipCode;
    private String district;
    private String state;
    private String city;

}
