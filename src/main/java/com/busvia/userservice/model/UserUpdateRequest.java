package com.busvia.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private   String firstName;
    private   String lastName;
    private   String email;
    private   String contact;
    private   String  gender;

}
