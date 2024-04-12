package com.busvia.userservice.service;

import com.busvia.userservice.client.AuthClient;
//import com.busvia.userservice.client.BusClient;
import com.busvia.userservice.client.BusRootClient;
import com.busvia.userservice.entity.*;
import com.busvia.userservice.model.*;
import com.busvia.userservice.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class UserServiceImp implements  UserService{

    @Autowired
    private  UserRepo userRepo;
    @Autowired
    private MoreDetailRepo moreRepo;
    @Autowired
    RestTemplate template;
    @Autowired
    BusRepo busRepo;
//    @Autowired
//    JavaMailSender mailSender;

    @Autowired
    WalletRepo walletRepo;

    @Autowired
    WalletHistoryRepo walletHistoryRepo;

    @Autowired
    AuthClient authClient;

    @Autowired
    UserMoreDetailsRepo userMoreDetailsRepo;

    @Autowired
    BusRootAndTimeRepo busRootAndTimeRepo;

    @Autowired
    BusRootClient busRootClient;

    @Qualifier("customPasswordEncoder")
    @Autowired
    PasswordEncoder passwordEncoder;

    private final String AUTH_SERVICE_URL = "http://localhost:9090/auth/register";


    @Override
    public UserEntity addUser(RegisterRequest request) {


        UserEntity userEntity =new UserEntity();

        userEntity.setFirstName(request.getFirstName());
        if(request.getRole()=="Operator"){
            userEntity.setLastName(request.getAuthorityEmail());
        }else{
            userEntity.setLastName(request.getLastName());
        }

        userEntity.setEmail(request.getEmail());
        userEntity.setContact(request.getContact());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRole(request.getRole());
        userEntity.setGender(request.getGender());
        userEntity.setBlock(true);

        UserEntity savedUser = userRepo.save(userEntity);

            WalletEntity walletEntity = new WalletEntity();
            walletEntity.setTotalAmount(0.0);
            walletEntity.setRefundAmount(0.0);
            walletEntity.setPayedAmount(0.0);
            walletEntity.setUserEntity(savedUser);
            walletEntity.setUserEmail(savedUser.getEmail());
            walletRepo.save(walletEntity);

        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(savedUser.getEmail());
        userInfo.setFirstName(savedUser.getFirstName());
        userInfo.setPassword(passwordEncoder.encode(request.getPassword()));
        userInfo.setRole(savedUser.getRole());



        UserRequest userRequest= new UserRequest(
                                                    userEntity.getUuid(),
                                                    userEntity.getFirstName(),
                                                    userEntity.getEmail(),
                                                    savedUser.getPassword(),
                                                    savedUser.getRole()
                                                  );
       String response =authClient.addNewUser(userRequest);
        System.out.println("Response from auth service: " + response);

        if(userRequest.getRole()=="[Operator]") {
            System.out.println("role is operator "+userRequest.getRole());
//            busClient.addNewOperator(userRequest);
        }


        return savedUser;
    }

    @Override
    public UserEntity updateUser(UserUpdateRequest request,UUID userUuid) {


        UserEntity userEntity  = userRepo.findById(userUuid).orElseThrow(()->new RuntimeException( "user not found by Sreejith "));
        System.out.println("user get "+userEntity);

        userEntity.setFirstName(request.getFirstName());

        userEntity.setLastName(request.getLastName());

        userEntity.setEmail(request.getEmail());
        userEntity.setContact(request.getContact());
        userEntity.setGender(request.getGender());

        System.out.println("User before saveing"+userEntity);

        UserEntity savedUser = userRepo.save(userEntity);
        System.out.println("Saved user : "+savedUser);
//
        UserInfo userInfo = new UserInfo();
        userInfo.setEmail(savedUser.getEmail());
        userInfo.setFirstName(savedUser.getFirstName());
        userInfo.setPassword(passwordEncoder.encode(savedUser.getPassword()));
        userInfo.setRole(savedUser.getRole());

        UserRequest userRequest= new UserRequest(
                userEntity.getUuid(),
                userEntity.getFirstName(),
                userEntity.getEmail(),
                savedUser.getPassword(),
                savedUser.getRole()
        );

        String response =authClient.updateUser(userRequest);
        System.out.println("Response from auth service: " + response);

        if(userRequest.getRole()=="[Operator]") {
            System.out.println("role is operator "+userRequest.getRole());

        }

        return savedUser;
    }

    @Override
    public MoreDetails addMoreDetails(MoreDetailRequest request,UserEntity userDetails) {

        MoreDetails moreDetails = new MoreDetails();
        moreDetails.setUniqueName(request.getUniqueName());
        moreDetails.setDistrict(request.getDistrict());
        moreDetails.setPincode(request.getPincode());
        moreDetails.setLocation(request.getLocation());
        moreDetails.setAuthorityType(request.getAuthorityType());
        moreDetails.setLicenceNo(request.getLicenceNo());
        moreDetails.setUser(userDetails);

        MoreDetails addedMoreDetails =moreRepo.save(moreDetails);


        return addedMoreDetails;
    }

    @Override
    public List<UserEntity> getALlUsers() {
       return userRepo.findAll();
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepo.deleteById(userId);
    }

    @Override
    public void blockUser(UUID uuid) {
            Optional<UserEntity>    userdata = userRepo.findById(uuid);
            if (userdata.isPresent()) {
                UserEntity user = userdata.get();
                user.setBlock(false);
              UserEntity userinfo   =   userRepo.save(user);
                UserInfo userData = new UserInfo();
                userData.setUserId(userinfo.getUuid());

                UserRequest userRequest = new UserRequest(
                    user.getUuid(),
                            user.getFirstName(),
                            user.getEmail(),
                            user.getPassword(),
                            user.getRole()
                );
                String response =authClient.blockUser(userRequest);
                System.out.println("Response from auth service: block " + response);

            } else {
                throw new EntityNotFoundException("User not found with ID: " + uuid);
            }

        }
    @Override
    public void unblockUser(UUID uuid) {
        Optional<UserEntity> userdata = userRepo.findById(uuid);
        if (userdata.isPresent()) {
            UserEntity user = userdata.get();
            user.setBlock(true);
            userRepo.save(user);
            UserEntity userinfo   =   userRepo.save(user);
            UserInfo userData = new UserInfo();
            userData.setUserId(userinfo.getUuid());

            UserRequest userRequest = new UserRequest(
                    user.getUuid(),
                    user.getFirstName(),
                    user.getEmail(),
                    user.getPassword(),
                    user.getRole()
            );
            String response =authClient.unblockUser(userRequest);
            System.out.println("Response from auth service: un-block " + response);
        } else {
            throw new EntityNotFoundException("User not found with ID: " + uuid);
        }


    }


    @Override
    public void getUserById(UUID userId) {
        userRepo.findById(userId);
    }

    @Override
    public List<UserEntity> findByFirstName(String firstName) {
        return userRepo.findByFirstNameContainingIgnoreCase(firstName);
    }



    //    @Override

    @Override
    public UserProfileResponse getUserByEmails(String email) {
        System.out.println("email: " + email);
        UserEntity user = userRepo.findByEmail(email).orElseThrow();

        UserProfileResponse userInfo = new UserProfileResponse();
        userInfo.setEmail(user.getEmail());
        userInfo.setContact(user.getContact());
        userInfo.setGender(user.getGender());
        userInfo.setLastName(user.getLastName());
        userInfo.setFirstName(user.getFirstName());
        userInfo.setUuid(user.getUuid());

        // Check if userMoreDetails is not null
        UserMoreDetailsEntity userMoreDetails = user.getUserMoreDetails();
        if (userMoreDetails != null) {
            userInfo.setAdditionalContact(userMoreDetails.getAdditionalContact());
            userInfo.setDistrict(userMoreDetails.getDistrict());
            userInfo.setZipCode(userMoreDetails.getZipCode());
            userInfo.setState(userMoreDetails.getState());
            userInfo.setCity(userMoreDetails.getCity());
            userInfo.setDob(userMoreDetails.getDob());
        } else {
            // If userMoreDetails is null, set empty values
            userInfo.setAdditionalContact("");
            userInfo.setDistrict("");
            userInfo.setZipCode("");
            userInfo.setState("");
            userInfo.setCity("");
        }

        return userInfo;
    }

    @Override
    public List<UserEntity> getUsersByRole(String role) {
        System.out.println("get by role service layer >"+role);
        return userRepo.findByRole(role);
    }

    @Override
    public UserEntity findByRegistredUserId(UUID uuid) {

        return userRepo.findById(uuid).orElseThrow(() -> new RuntimeException("User not found with UUID: " + uuid));
    }

    @Override
    public List<UserEntity> getAllAuthorityByRole(String role) {

        List<UserEntity> userEntity= userRepo.findByRole(role);
     return  userEntity;
    }

    @Override
    public List<MoreDetails> getAuthorityMoreDetails(String userId) {
        UUID uuid= UUID.fromString(userId);
        List<MoreDetails> moreDetails= moreRepo.findByUserUuid(uuid);

        return  moreDetails;
    }

    @Override
    public List<UserEntity> getDriverByAuthorityMail(String mail) {
        List<UserEntity> driverList =  userRepo.findAllByLastNameContaining(mail);
        return driverList ;
    }

    @Override
    public void saveBus(BusRequest busRequest) {
        System.out.println("BUS UUID IN USER SERVICE"+busRequest.getUuid());
        BusInfo bus = new BusInfo();
        bus.setBusName(busRequest.getBusName());
        bus.setBusNumber(busRequest.getBusNumber());
        bus.setBusType(busRequest.getBusType());
        bus.setUuid(busRequest.getUuid());
        bus.setEmail(busRequest.getEmail());
        bus.setTotalSeats(busRequest.getTotalSeats());
        bus.setAvailableSeats(busRequest.getAvailableSeats());
        bus.setFare(busRequest.getFare());
        bus.setCategory(busRequest.getCategory());
        bus.setDoubleSeatCount(busRequest.getDoubleSeatCount());
        bus.setThirdRowSeatCount(busRequest.getThirdRowSeatCount());
        bus.setUpperSeat(busRequest.getUpperSeat());
        bus.setLowerSeat(busRequest.getLowerSeat());
        bus.setActive(true);
        busRepo.save(bus);
    }

    @Override
    public void updateBusData(BusRequest busRequest) {
        BusInfo bus = new BusInfo();
        bus.setBusName(busRequest.getBusName());
        bus.setBusNumber(busRequest.getBusNumber());
        bus.setBusType(busRequest.getBusType());
        bus.setUuid(busRequest.getUuid());
        bus.setEmail(busRequest.getEmail());
        bus.setTotalSeats(busRequest.getTotalSeats());
        bus.setAvailableSeats(busRequest.getAvailableSeats());
        bus.setFare(busRequest.getFare());
        bus.setCategory(busRequest.getCategory());
        bus.setDoubleSeatCount(busRequest.getDoubleSeatCount());
        bus.setThirdRowSeatCount(busRequest.getThirdRowSeatCount());
        bus.setUpperSeat(busRequest.getUpperSeat());
        bus.setLowerSeat(busRequest.getLowerSeat());
//        set user for existing bus
        UUID busId =busRequest.getUuid();
        BusInfo busInfo =busRepo.findById(busId).orElseThrow();
        bus.setUser(busInfo.getUser());
        System.out.println("driver : "+busInfo.getUser());
        busRepo.save(bus);
    }


    @Override
    public void updateBusRoot(BusRootReq busRootReq) {
        UUID rootId=busRootReq.getUuid();
        System.out.println("Root Id "+rootId);
      BusRootAndTime busRootAndTime  =busRootAndTimeRepo.findById(rootId).orElseThrow(()-> new RuntimeException("Root not found in this id "));
        System.out.println("Root data  "+busRootAndTime);
        if(busRootAndTime!=null){
            busRootAndTime.setSourceLocation(busRootReq.getSourceLocation());
            busRootAndTime.setDestinationLocation(busRootReq.getDestinationLocation());
            busRootAndTime.setArrivalTime(busRootReq.getArrivalTime());
            busRootAndTime.setDepartureTime(busRootReq.getDepartureTime());
            busRootAndTime.setArrivalDate(busRootReq.getArrivalDate());
            busRootAndTime.setDepartureDate(busRootReq.getDepartureDate());
            busRootAndTime.setPerdayTrip(busRootReq.getPerdayTrip());
            busRootAndTime.setTotalHour(busRootReq.getTotalHour());
            busRootAndTime.setRootType(busRootReq.getRootType());
            BusRootAndTime rootUpdatedInfo=busRootAndTimeRepo.save(busRootAndTime);
            System.out.println("Updated root in USer service "+ rootUpdatedInfo);
        }
    }


    @Override
    public void addBusToDriver(UUID driverUUid, UUID busUuid) {

      Optional<UserEntity> userEntity  =userRepo.findById(driverUUid);
      UserEntity driver=userEntity.get();
        System.out.println(userEntity);


      Optional<BusInfo> busInfo=busRepo.findById(busUuid);
            BusInfo busData = busInfo.get();

        System.out.println("Bus uuid "+busData.getUuid());
        System.out.println(busData);
            driver.setBusId(busData.getUuid());
            userRepo.save(driver);

            busData.setUser(driver);
            busRepo.save(busData);


    }

    @Override
    public List<BusInfo> getALlBusByMail(String email) {
        return busRepo.findAllByEmail(email);
    }

    @Override
    public Optional<BusInfo> findDriverBusById(String busId) {
        UUID uuid = UUID.fromString(busId);
        return busRepo.findById(uuid);
    }

    @Override
    public List<BusInfo> getBusByDriverId(String driverMail) {
        Optional<UserEntity> user = userRepo.findByEmail(driverMail);
        if (!user.isPresent()) {
            throw new RuntimeException("Driver with email " + driverMail + " not found");
        }
            UserEntity driver = user.get();
            UUID busId = driver.getBusId();
        return busRepo.findById(busId)
                .map(Collections::singletonList)
                .orElseThrow(() -> new RuntimeException("Bus not found"));
    }


    @Override
    public BusRootAndTime addRootToBus(UUID busId, BusRootRequest busRoot) {

        busRoot.setBusId(busId);
        UUID busUuid = busId;

        List<BusRootAndTime> busRoots = busRootAndTimeRepo.findByBusId(busUuid);

        if (busRoots.size()==1) {
            BusRootAndTime existBusRoot = busRoots.get(0);

            existBusRoot.setSourceLocation(busRoot.getSourceLocation());
            existBusRoot.setDestinationLocation(busRoot.getDestinationLocation());
            existBusRoot.setArrivalTime(busRoot.getArrivalTime());
            existBusRoot.setArrivalDate(busRoot.getArrivalDate());
            existBusRoot.setDepartureTime(busRoot.getDepartureTime());
            existBusRoot.setArrivalDate(busRoot.getArrivalDate());
            existBusRoot.setBusId(busUuid);
            existBusRoot.setPerdayTrip(busRoot.getPerdayTrip());
            existBusRoot.setTotalHour(busRoot.getTotalHour());
            existBusRoot.setRootType(busRoot.getRootType());

            if (busRoots!=null) {

                BusRootRequest busRootRequest = new BusRootRequest(
                        existBusRoot.getUuid(),
                        existBusRoot.getSourceLocation(),
                        existBusRoot.getDestinationLocation(),
                        existBusRoot.getDepartureTime(),
                        existBusRoot.getArrivalTime(),
                        existBusRoot.getDepartureDate(),
                        existBusRoot.getArrivalDate(),
                        existBusRoot.getTotalHour(),
                        existBusRoot.getPerdayTrip(),
                        existBusRoot.getRootType(),
                        existBusRoot.getBusId()
                );


                busRootClient.addBusRootAndTime(busRootRequest);

            }

            return  busRootAndTimeRepo.save(existBusRoot);
        } else {

            BusInfo bus = busRepo.findById(busId)
                    .orElseThrow(() -> new RuntimeException("Bus not found with ID: " + busId));

            BusRootAndTime busRootAndTime = new BusRootAndTime();
            busRootAndTime.setSourceLocation(busRoot.getSourceLocation());
            busRootAndTime.setDestinationLocation(busRoot.getDestinationLocation());
            busRootAndTime.setDepartureDate(busRoot.getDepartureDate());
            busRootAndTime.setArrivalDate(busRoot.getArrivalDate());
            busRootAndTime.setDepartureTime(busRoot.getDepartureTime());
            busRootAndTime.setArrivalTime(busRoot.getArrivalTime());
            busRootAndTime.setBusId(busUuid);
            busRootAndTime.setPerdayTrip(busRoot.getPerdayTrip());
            busRootAndTime.setTotalHour(busRoot.getTotalHour());
            busRootAndTime.setBusInfo(bus);

            BusRootAndTime savedBusRoot = busRootAndTimeRepo.save(busRootAndTime);

            bus.getBusRoots().add(savedBusRoot);
            busRepo.save(bus);

            if (bus!=null) {

                BusRootRequest busRootRequest = new BusRootRequest(
                savedBusRoot.getUuid(),
                        savedBusRoot.getSourceLocation(),
                        savedBusRoot.getDestinationLocation(),
                        savedBusRoot.getDepartureTime(),
                        savedBusRoot.getArrivalTime(),
                        savedBusRoot.getDepartureDate(),
                        savedBusRoot.getArrivalDate(),
                        savedBusRoot.getTotalHour(),
                        savedBusRoot.getPerdayTrip(),
                        savedBusRoot.getRootType(),
                        savedBusRoot.getBusId());
              busRootClient.addBusRootAndTime(busRootRequest);

            }

            return  savedBusRoot;
        }

       }

    @Override
    public List<BusRootAndTime> findBusRootByBusId(String busId) {
        UUID busUuid = UUID.fromString(busId);
        return busRootAndTimeRepo.findByBusId(busUuid);
    }

    @Override
    public BusRootAndTime findBusRootByBusUuid(String uuid) {
        UUID routeUuid = UUID.fromString(uuid);
        System.out.println("routeUuid "+routeUuid);
        Optional<BusRootAndTime> busRootAndTime=  busRootAndTimeRepo.findById(routeUuid);
        BusRootAndTime busRootAndTimeList= busRootAndTime.get();
        System.out.println("routeUuid data "+busRootAndTimeList);

        return busRootAndTimeList;
    }


    @Override
    public void addMoreUserDetails(UserMoreDetailsRequest userDetailsDTO) {
        System.out.println("h 1"+userDetailsDTO);
        UserMoreDetailsEntity user = new UserMoreDetailsEntity();
        System.out.println("h 2" +user);
        user.setAdditionalContact(userDetailsDTO.getAdditionalContact());
        user.setZipCode(userDetailsDTO.getZipCode());
        user.setDistrict(userDetailsDTO.getDistrict());
        user.setState(userDetailsDTO.getState());
        user.setCity(userDetailsDTO.getCity());
//        user.setDob((Date) userDetailsDTO.getDob());
        System.out.println("h 3" +user);
        UUID userId= UUID.fromString(userDetailsDTO.getUserId());
        System.out.println("h 4" +userId);
        UserEntity userEntity = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println("h 5" +userEntity);
        user.setUserEntity(userEntity);
        userEntity.setUserMoreDetails(user);
//            userRepo.save(userEntity);
        userRepo.save(userEntity);
        // Save the entity
        userMoreDetailsRepo.save(user);

    }

    @Override
    public void blockBus(BusBlockRequest busBlockRequest) {

        BusInfo bus =  busRepo.findById(busBlockRequest.getUuid())
                .orElseThrow(()->new RuntimeException( "Bus @ not found in bus service "));

        bus.setActive(false);
        BusInfo busInfo=busRepo.save(bus);
        System.out.println("bus - BlockInfo : "+busInfo);

    }

    @Override
    public void unblockBus(BusBlockRequest busBlockRequest) {

        BusInfo bus =  busRepo.findById(busBlockRequest.getUuid())
                .orElseThrow(()->new RuntimeException( "Bus @ not found in bus service "));

        bus.setActive(true);
        BusInfo busInfo=busRepo.save(bus);
        System.out.println("bus - BlockInfo : "+busInfo);

    }

    @Override
    public UserEntity getUserByEmail(String userMail) {
      UserEntity user = userRepo.findByEmail(userMail)

              .orElseThrow(()->new RuntimeException( "user @ not found in bus service "));
        System.out.println("user by email "+user);
        return user;
    }

    @Override
    public WalletEntity updateWallet(double totalFare, String email, String bookId) {
        UUID bookingId = UUID.fromString(bookId);
        WalletEntity wallet = walletRepo.findByUserEmail(email);
        System.out.println("UPDATE WALLET ");

        if (wallet != null) {
            if (!wallet.isCashAdded()) {
                System.out.println("IFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF");

                double updatedCash = wallet.getTotalAmount() + totalFare;
                wallet.setTotalAmount(updatedCash);
                wallet.setRefundAmount(updatedCash);
                wallet.setCashAdded(true); // Mark cash as added

                // Create wallet history entry
                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setWallet(wallet);
                walletHistory.setCashAdded(true);
                walletHistory.setBookingUuid(bookingId);
                walletHistory.setRefundBookingAmount(totalFare);
                WalletHistory savedWalletHistory = walletHistoryRepo.save(walletHistory);
                wallet.getWalletHistoryList().add(savedWalletHistory);
                System.out.println("savedWalletHistory initial  " + savedWalletHistory);

                // Save updated wallet
                return walletRepo.save(wallet);
            } else {
                boolean isSatisfied = false;
                System.out.println("ELSEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEYYYY");
                List<WalletHistory> walletHistoryList = wallet.getWalletHistoryList();
                for (WalletHistory walletHist : walletHistoryList) {
                    if (!walletHist.getBookingUuid().equals(bookingId)) {
                        System.out.println("Wallet refund ------------getBookingUuid-- "+walletHist.getBookingUuid()+" " +" BOOKIid "+ bookingId  );
                        isSatisfied = false;
//                        break;
                    }else{
                        System.out.println("BREAK   *********************************************");
                        isSatisfied = true;
                        break;
                    }
                }
                if (!isSatisfied) {
                    System.out.println("SATISFIED  # # # # # &&&"  );
                    double updatedCash = wallet.getTotalAmount() + totalFare;
                    wallet.setTotalAmount(updatedCash);
                    wallet.setRefundAmount(updatedCash);

                    // Create wallet history entry
                    WalletHistory walletHistory = new WalletHistory();
                    walletHistory.setWallet(wallet);
                    walletHistory.setCashAdded(true);
                    walletHistory.setBookingUuid(bookingId);
                    walletHistory.setRefundBookingAmount(totalFare);
                    walletHistory.setWalletStatus("REFUND");
                    walletHistory.setTransactionDate(new Date());

                    WalletHistory savedWalletHistory = walletHistoryRepo.save(walletHistory);
                    wallet.getWalletHistoryList().add(savedWalletHistory);
                    System.out.println("savedWalletHistory else " + savedWalletHistory);
                    return walletRepo.save(wallet);
                } else {
                    System.out.println("Wallet refund amount already added");
                    return wallet;
                }
            }
        }
        return wallet;
    }

    @Override
    public WalletEntity updateWalletAfterBooking(double bookedAmount, String email, String bookId) {
        System.out.println("UPDATE WALLET AFTER BOOKING");
        UUID bookingId = UUID.fromString(bookId);
        WalletEntity wallet = walletRepo.findByUserEmail(email);
        if (wallet != null) {
            System.out.println( "bookingCash  up[dated and save to wallet");
            double updatedCash = wallet.getTotalAmount() - bookedAmount;
            wallet.setTotalAmount(updatedCash);
            wallet.setPayedAmount(bookedAmount);
            List<WalletHistory> walletHistoryList = wallet.getWalletHistoryList();

            boolean isSatisfied = false;
            for (WalletHistory walletHist : walletHistoryList) {
                if (!walletHist.getBookingUuid().equals(bookingId)) {
                    System.out.println("Wallet refund ------------getBookingUuid-- "+walletHist.getBookingUuid()+" " +" BOOKIid "+ bookingId  );
                    isSatisfied = false;
                }else{
                    System.out.println("BREAK   *********************************************");
                    isSatisfied = true;
                    break;
                }
            }
            if (!isSatisfied) {
                System.out.println("SATISFIED  # # # # #"  );
                WalletHistory walletHistory = new WalletHistory();
                walletHistory.setWallet(wallet);
                walletHistory.setCashAdded(false);
//                walletHistory.setBookingUuid(bookingId);
                walletHistory.setRefundBookingAmount(bookedAmount);
                walletHistory.setWalletStatus("PAID");
                walletHistory.setTransactionDate(new Date());

                WalletHistory savedWalletHistory = walletHistoryRepo.save(walletHistory);
                wallet.getWalletHistoryList().add(savedWalletHistory);
                System.out.println("savedWalletHistory else " + savedWalletHistory);
                WalletEntity walletInfo = walletRepo.save(wallet);
//                if(savedWalletHistory.getWalletStatus()=="REFUND")
                return walletInfo;
            } else {
                System.out.println("Wallet refund amount already added");
                return wallet;
            }


        }else{

            throw  new RuntimeException("Wallet not found ");

        }



    }


    @Override
    public WalletEntity getWalletByEmail(String email) {

       WalletEntity  wallet = walletRepo.findByUserEmail(email);
        System.out.println("user wallet get by email "+wallet);
        return wallet;
    }

    @Override
    public List<WalletHistory> getALlUserWalletHistory(String email) {

     WalletEntity userWallet  =  walletRepo.findByUserEmail(email);
       List<WalletHistory> WalletHistory =userWallet.getWalletHistoryList();
        System.out.println("All user wallet history "+WalletHistory);
        return WalletHistory;
    }




}

