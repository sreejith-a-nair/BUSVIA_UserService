package com.busvia.userservice.model;

import lombok.Data;

import java.util.Date;

@Data
public class UpdateWalletAfterBookingReq {
    private double totalFare;
    private String email;
    private String bookingId;
    private Date bookingDate;
    private String status;

}
