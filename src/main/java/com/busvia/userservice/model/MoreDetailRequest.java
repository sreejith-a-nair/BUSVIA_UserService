package com.busvia.userservice.model;

import lombok.Data;

@Data
public class MoreDetailRequest {
    private String userId;
    private String uniqueName;
    private String authorityType;
    private String location;
    private String district;
    private String pincode;
    private  String licenceNo;
}
