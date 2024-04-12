package com.busvia.userservice.service;

import com.busvia.userservice.entity.*;
import com.busvia.userservice.model.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public interface UserService {
     UserEntity addUser(RegisterRequest request);

    UserEntity updateUser(UserUpdateRequest request,UUID userUuid);

    MoreDetails addMoreDetails(MoreDetailRequest request,UserEntity userdata);

     List<UserEntity> getALlUsers();

     void deleteUser(UUID userId);

     void blockUser( UUID uuid);
     void unblockUser(UUID uuid);

     void getUserById(UUID userId);

     List<UserEntity> findByFirstName(String firstName);

//    UserEntity getUserByEmail(String email);
    UserProfileResponse getUserByEmails(String email);

     List<UserEntity> getUsersByRole(String role);

    UserEntity findByRegistredUserId(UUID uuid);

    List<UserEntity> getAllAuthorityByRole(String role);

    List<MoreDetails> getAuthorityMoreDetails(String userId);

    List<UserEntity> getDriverByAuthorityMail(String mail);

    void saveBus(BusRequest busRequest);

    void addBusToDriver(UUID drievrUUId, UUID busUUId);

    List<BusInfo> getALlBusByMail(String email);

    Optional<BusInfo> findDriverBusById(String busId);

    List<BusInfo> getBusByDriverId(String driverMail);

    BusRootAndTime addRootToBus(UUID busId, BusRootRequest busRoot);

    List<BusRootAndTime> findBusRootByBusId(String busId);

    BusRootAndTime findBusRootByBusUuid(String uuid);

    void updateBusData(BusRequest busRequest);

    void addMoreUserDetails(UserMoreDetailsRequest userMoreDetailsRequest);

    void blockBus(BusBlockRequest busBlockRequest);

    void unblockBus(BusBlockRequest busBlockRequest);

    UserEntity getUserByEmail(String userMail);

    WalletEntity updateWallet(double totalFare, String email,String bookingId);

    WalletEntity updateWalletAfterBooking(double totalFare, String email, String bookingId);

    WalletEntity getWalletByEmail(String email);

    List<WalletHistory> getALlUserWalletHistory(String email);

    void updateBusRoot(BusRootReq busRootReq);



//    boolean sendMail(String toEmail,
//                     String subject,
//                     String body);
//
//   UserEntity findByEmail(String email);

//     ResponseEntity<String> changePassword(Map<String,String> requestMap);
}
