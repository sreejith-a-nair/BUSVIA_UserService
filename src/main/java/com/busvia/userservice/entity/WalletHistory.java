package com.busvia.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.PrivateKey;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WalletHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private double refundBookingAmount;

    private UUID bookingUuid;

    private boolean isCashAdded;

    private  String walletStatus;

    private Date transactionDate;

    @ManyToOne
    @JoinColumn(name = "wallet_id")
    @JsonIgnore
    private WalletEntity wallet;


}
