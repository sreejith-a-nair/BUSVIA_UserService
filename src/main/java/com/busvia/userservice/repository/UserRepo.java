package com.busvia.userservice.repository;

import com.busvia.userservice.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo  extends JpaRepository<UserEntity, UUID> {

    List<UserEntity> findByFirstNameContainingIgnoreCase(String firstName);

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByRole(String role);

//    @Query("SELECT u FROM UserEntity u WHERE u.lastName = :lastName")
     List<UserEntity> findAllByLastNameContaining(String authorityEmail);




}