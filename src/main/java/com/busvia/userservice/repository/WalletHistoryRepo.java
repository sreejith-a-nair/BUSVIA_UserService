package com.busvia.userservice.repository;

import com.busvia.userservice.entity.WalletHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface WalletHistoryRepo extends JpaRepository<WalletHistory , UUID> {
}
