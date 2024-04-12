package com.busvia.userservice.model;

import lombok.Data;

@Data
public class UpdateWalletRequest {

    private double totalFare;
    private String email;
    private String bookingId;
}
