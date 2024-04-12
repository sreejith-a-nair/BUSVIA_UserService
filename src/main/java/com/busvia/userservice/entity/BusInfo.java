package com.busvia.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;


import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusInfo {
    @Id
    private UUID uuid;
    private  String busName;
    private  String busNumber;
    private  String busType;
    private int totalSeats;
    private int availableSeats;
    private double fare;
    private String category;
    private int doubleSeatCount;
    private int thirdRowSeatCount;
    private String email;
    private int upperSeat;
    private  int lowerSeat;
    private  boolean isActive;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;


    @OneToMany(mappedBy = "busInfo", cascade = CascadeType.ALL)
    private List<BusRootAndTime> busRoots;


    @Override
    public String toString() {
        return "BusInfo{" +
                "uuid=" + uuid +
                ", busName='" + busName + '\'' +
                ", busNumber='" + busNumber + '\'' +
                ", busType='" + busType + '\'' +
                ", totalSeats=" + totalSeats +
                ", availableSeats=" + availableSeats +
                ", fare=" + fare +
                ", category='" + category + '\'' +
                ", doubleSeatCount=" + doubleSeatCount +
                ", thirdRowSeatCount=" + thirdRowSeatCount +
                ", email='" + email + '\'' +
                ", upperSeat='" + upperSeat + '\'' +
                ", lowerSeat='" + lowerSeat + '\'' +
                ", isActive='" + isActive + '\'' +
                '}';
    }

}
