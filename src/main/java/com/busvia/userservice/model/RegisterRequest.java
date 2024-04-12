package com.busvia.userservice.model;
import lombok.Data;

@Data
public class RegisterRequest {

    private   String firstName;
    private   String lastName;
    private   String email;
    private   String contact;
    private   String password;
    private   String  confirmPassword;
    private   String  gender;
    private   String  role;
    private  String authorityEmail;

}
