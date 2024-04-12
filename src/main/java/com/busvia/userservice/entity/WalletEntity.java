package com.busvia.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class WalletEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID uuid;

    private double totalAmount;

    private double refundAmount;

    private double payedAmount;


    private  String userEmail;

    private boolean cashAdded;

//    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL)
//    private UserEntity user;
    @OneToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserEntity userEntity;

    @OneToMany(mappedBy = "wallet")
    private List<WalletHistory> walletHistoryList;


    @Override
    public String toString() {
        return "WalletEntity{" +
                "uuid=" + uuid +
                ", totalAmount=" + totalAmount +
                ", refundAmount=" + refundAmount +
                ", userEmail='" + userEmail + '\'' +
                ", cashAdded=" + cashAdded +
                '}';
    }


}
