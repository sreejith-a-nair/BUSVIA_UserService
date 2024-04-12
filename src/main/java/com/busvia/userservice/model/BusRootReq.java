package com.busvia.userservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusRootReq {
    private UUID uuid;
    private String sourceLocation;
    private String destinationLocation;
    private String departureTime;
    private String arrivalTime;
    private Date departureDate;
    private Date arrivalDate;
    private String totalHour;
    private int perdayTrip;
    private String rootType;
}