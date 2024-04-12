package com.busvia.userservice.repository;

import com.busvia.userservice.entity.WalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletRepo extends JpaRepository<WalletEntity , UUID> {

    WalletEntity findByUserEmail(String email);
//    WalletEntity findByBookingId(UUID bookingId);
}
