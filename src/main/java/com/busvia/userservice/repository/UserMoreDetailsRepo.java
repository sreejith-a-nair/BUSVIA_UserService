package com.busvia.userservice.repository;

import com.busvia.userservice.entity.MoreDetails;
import com.busvia.userservice.entity.UserMoreDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserMoreDetailsRepo extends JpaRepository<UserMoreDetailsEntity, UUID> {
}
