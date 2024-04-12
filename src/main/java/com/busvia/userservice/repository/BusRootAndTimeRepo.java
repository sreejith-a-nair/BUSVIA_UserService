package com.busvia.userservice.repository;


import com.busvia.userservice.entity.BusRootAndTime;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BusRootAndTimeRepo extends JpaRepository<BusRootAndTime , UUID> {
    List<BusRootAndTime> findByBusId(UUID busId);

}
