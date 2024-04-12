package com.busvia.userservice.model;

import lombok.Data;

@Data
public class RegisterResponse {

    private   String firstName;
    private   String lastName;
    private   String email;
    private   String contact;
    private   String password;
    private   String role;
    private   String gender;
}
