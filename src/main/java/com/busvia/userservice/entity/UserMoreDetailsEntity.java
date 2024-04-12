package com.busvia.userservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.sql.Date;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserMoreDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID uuid;

    private Date dob;
    private String additionalContact;
    private String zipCode;
    private String district;
    private String state;
    private String city;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_entity_id")
    @JsonIgnore
    private UserEntity userEntity;

}
