package com.busvia.userservice.model;

import lombok.Data;

@Data
public class BusToDriverRequest {
    private  String authorityId;
    private  String selectedBusId;
}