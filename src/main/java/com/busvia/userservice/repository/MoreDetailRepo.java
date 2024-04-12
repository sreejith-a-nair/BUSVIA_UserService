package com.busvia.userservice.repository;

import com.busvia.userservice.entity.MoreDetails;
import com.busvia.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MoreDetailRepo extends JpaRepository<MoreDetails , UUID> {


    List<MoreDetails> findByUserUuid(UUID userId);


}
