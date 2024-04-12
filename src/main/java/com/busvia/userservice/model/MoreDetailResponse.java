package com.busvia.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoreDetailResponse {
    private String uniqueName;
    private String authorityType;
    private String location;
    private String district;
    private String pincode;
    private  String licenceNo;
}
