package com.busvia.userservice.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.List;
import java.util.UUID;

    @Entity
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public class UserEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        private UUID uuid;
        private  String firstName;
        private  String lastName;
        @Column(unique = true)
        private  String email;
        private  String role;

        @Column(unique = true)
        private  String contact;

        private  String password;
        private boolean isBlock;
        private String gender;

        @Column(name = "bus_id")
        private UUID busId;

    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private UserMoreDetailsEntity userMoreDetails;

//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "wallet_id", referencedColumnName = "uuid")
//    private WalletEntity wallet;
    @OneToOne(mappedBy = "userEntity", cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id")
    private WalletEntity wallet;

        @Override
        public String toString() {
            return "UserEntity{" +
                    "uuid=" + uuid +
                    ", firstName='" + firstName + '\'' +
                    ", lastName='" + lastName + '\'' +
                    ", email='" + email + '\'' +
                    ", role='" + role + '\'' +
                    ", contact='" + contact + '\'' +
                    ", password='" + password + '\'' +
                    ", isBlock=" + isBlock +
                    ", gender='" + gender + '\'' +
                    '}';
        }

}
