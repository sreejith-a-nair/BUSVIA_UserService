package com.busvia.userservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MoreDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;
    private String uniqueName;
    private String authorityType;
    private String location;
    private String district;
    private String pincode;
    @Column(unique = true)
    private String licenceNo;

    @OneToOne
    @JoinColumn(name = "user_uuid")
    private UserEntity user;

    @Override
    public String toString() {
        return "MoreDetails{" +
                "uuid=" + uuid +
                ", uniqueName='" + uniqueName + '\'' +
                ", authorityType='" + authorityType + '\'' +
                ", location='" + location + '\'' +
                ", district='" + district + '\'' +
                ", pincode='" + pincode + '\'' +
                ", licenceNo='" + licenceNo + '\'' +
                ", user=" + (user != null ? user.getUuid() : null) + // Assuming UserEntity has getUuid() method
                '}';
    }
}
