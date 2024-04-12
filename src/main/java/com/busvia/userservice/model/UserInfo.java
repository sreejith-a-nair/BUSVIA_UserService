package com.busvia.userservice.model;

import jdk.jfr.DataAmount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {

    private UUID userId;
    private String firstName;
    private String lastName;
    private String email;
    private String contact;
    private String password;
    private  String role;
}
