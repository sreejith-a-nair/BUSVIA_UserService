package com.busvia.userservice.repository;

import com.busvia.userservice.entity.BusInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BusRepo extends JpaRepository<BusInfo, UUID> {
    List<BusInfo> findAllByEmail(String email);

}
