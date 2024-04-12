package com.busvia.userservice.controller;


import com.busvia.userservice.client.AuthClient;
import com.busvia.userservice.entity.*;
import com.busvia.userservice.model.*;

import com.busvia.userservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthClient authClient;


    private static final Logger LOGGER = Logger.getLogger(String.valueOf(UserInfo.class));

    //  @CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE})
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody RegisterRequest request) {

        try {
            System.out.println("try");
            UserEntity userInfo = userService.addUser(request);
            System.out.println("try   : " + userInfo);

            return ResponseEntity.status(HttpStatus.CREATED).body(userInfo);
        } catch (DataIntegrityViolationException e) {
            System.out.println("catch" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or contact already exists.");
        }

    }


    @PostMapping("/updateUser/{userId}")
    public ResponseEntity<?> updateUser(@RequestBody UserUpdateRequest request, @PathVariable String userId) {
        System.out.println(userId);
        UUID userUuid = UUID.fromString(userId);
        try {
            System.out.println("try");
            UserEntity userInfo = userService.updateUser(request, userUuid);
            System.out.println("try   : " + userInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body(userInfo);
        } catch (DataIntegrityViolationException e) {
            System.out.println("catch" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or contact already exists.");
        }

    }

    @PostMapping("/more-details")
    public ResponseEntity<MoreDetails> moreDetail(@RequestBody MoreDetailRequest request) {
        UUID userId = UUID.fromString(request.getUserId());
        UserEntity userEntity = userService.findByRegistredUserId(userId);
        MoreDetails moreDetails = userService.addMoreDetails(request, userEntity);
        if (moreDetails != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(moreDetails);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(moreDetails);
        }
    }

    @GetMapping("getAllDriverByAuthorityMail/{mail}")
    public ResponseEntity<List<UserEntity>> getDriverByAuthorityMail(@PathVariable String mail) {
        List<UserEntity> driver = userService.getDriverByAuthorityMail(mail);
        return ResponseEntity.ok(driver);
    }


    @GetMapping("/profile/{email}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable String email) {
        System.out.println("user email " + email);
        UserProfileResponse user = userService.getUserByEmails(email);


        System.out.println(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/blockUser/{userId}")
    public ResponseEntity<String> blockUser(@PathVariable String userId) {
        System.out.println("BLOCK " + userId);
        try {
            UUID userUuid = UUID.fromString(userId);
            userService.blockUser(userUuid);
            return ResponseEntity.ok("User blocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to block user: " + e.getMessage());
        }
    }

    @PostMapping("/unblockUser/{userId}")
    public ResponseEntity<String> unblockUser(@PathVariable String userId) {
        System.out.println("UN-BLOCK " + userId);
        try {
            UUID userUuid = UUID.fromString(userId);
            userService.unblockUser(userUuid);
            return ResponseEntity.ok("User unblocked successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to unblock user: " + e.getMessage());
        }
    }


    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAll")
    public List<UserEntity> getAll() {
        List<UserEntity> users = userService.getALlUsers();
        return users;
    }

    @GetMapping("/getByRole")
    public List<UserEntity> getByRole(@RequestParam String role) {

        return userService.getUsersByRole(role);
    }

    @GetMapping("/getAllAuthority")
    public ResponseEntity<List<UserEntity>> getAllAuthority(@RequestParam String role) {

        List<UserEntity> authorities = userService.getAllAuthorityByRole(role);
        return ResponseEntity.ok(authorities);
    }

    @GetMapping("/getAllAuthorityByRole")
    public ResponseEntity<List<UserEntity>> getAllAuthorityByRole(@RequestParam String role) {
        System.out.println("Authority role is By Driver " + role);
        List<UserEntity> authorities = userService.getAllAuthorityByRole(role);
        return ResponseEntity.ok(authorities);
    }


    @GetMapping("/getAuthorityMoreDetails")
    public ResponseEntity<List<MoreDetails>> getAuthorityMoreDetails(@RequestParam String userId) {

        List<MoreDetails> moreDetails = userService.getAuthorityMoreDetails(userId);
        System.out.println("Authority More details:  " + moreDetails);
        return ResponseEntity.ok(moreDetails);
    }


    @DeleteMapping("/unBlockUser/{userId}")
    public ResponseEntity<Void> unBlockUser(@PathVariable UUID userId) {

        userService.unblockUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getById(@PathVariable UUID userId) {
        System.out.println("deleting" + userId);
        userService.getUserById(userId);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/addBus")
    public String addBus(@RequestBody BusRequest busRequest) {
        System.out.println("BUS REGISTER WORK " + busRequest);
        if (busRequest != null) {
            userService.saveBus(busRequest);
            return "Bus added successfully";
        }
        return "Request is empty";
    }

    @PostMapping("/updateBus")
    public String updateBus(@RequestBody BusRequest busRequest) {
        System.out.println("BUS REGISTER WORK " + busRequest);
        if (busRequest != null) {
            userService.updateBusData(busRequest);
            return "Bus updated successfully";
        }
        return "Request updated is empty";
    }

    @PostMapping("/updateRoot")
    public String updateRoot(@RequestBody BusRootReq busRootReq) {
        System.out.println("BUS REGISTER WORK " + busRootReq);
        if (busRootReq != null) {
            userService.updateBusRoot(busRootReq);
            return "Bus updated successfully";
        }
        return "Request updated is empty";
    }

    @PostMapping("/addBusToDriver")
    public ResponseEntity<String> addBusToDriver(@RequestBody BusToDriverRequest request) {
        try {
            UUID drievrUUId = UUID.fromString(request.getAuthorityId());
            UUID busUuid = UUID.fromString(request.getSelectedBusId());

            userService.addBusToDriver(drievrUUId, busUuid);

            return ResponseEntity.ok().body("{\"message\": \"Bus added to driver successfully\"}");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add bus to driver: " + e.getMessage());
        }
    }

    @GetMapping("/getAllBusByEmail")
    public List<BusInfo> getAllBusByMail(@RequestParam(required = false) String email) {
        List<BusInfo> buses = userService.getALlBusByMail(email);
        return buses;
    }

    @GetMapping("/findDriverBusById")
    public ResponseEntity<BusInfo> findDriverBusById(@RequestParam("busId") String busId) {
        try {
            System.out.println("BUS ID IS " + busId);
            Optional<BusInfo> busResponse = userService.findDriverBusById(busId);
            BusInfo driverBus = busResponse.get();
            System.out.println("BUS details ** " + driverBus);
            if (busResponse != null) {
                return ResponseEntity.ok().body(driverBus);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/getBUSByDriverId")
    public List<BusInfo> getBUSByDriverId(@RequestParam("driverMail") String driverMail) {
        System.out.println("GET BUS BY DRIVER " + driverMail);
        List<BusInfo> driverBuses = userService.getBusByDriverId(driverMail);
        System.out.println("DRIVER BUSES: " + driverBuses);
        return driverBuses;
    }

    @PostMapping("/addBusRootAndTime/{busId}")
    public ResponseEntity<String> addBusRootAndTime(@RequestBody BusRootRequest routeTimeData,
                                                    @PathVariable("busId") String busId) throws ParseException {
        System.out.println("DEPARTURE TIME 1:  "+routeTimeData.getDepartureTime());
        System.out.println("ARRIVAL TIME 1:  "+routeTimeData.getArrivalTime());


//
//        String departureTime = "11:00"; // Example departure time string
//        String arrivalTime = "03:00 PM";    // Example arrival time string

//        LocalTime departureLocalTimes = LocalTime.parse(routeTimeData.getDepartureTime(), DateTimeFormatter.ofPattern("hh:mm"));
//        System.out.println("DEPARTURE TIME 03:  "+departureLocalTimes);
//        LocalTime departureLocalTime = LocalTime.parse(departureTime, DateTimeFormatter.ofPattern("hh:mm"));
//        System.out.println("DEPARTURE TIME 03:  "+departureLocalTime);
//        LocalTime arrivalLocalTime = LocalTime.parse(arrivalTime, DateTimeFormatter.ofPattern("hh:mm a"));
//        System.out.println("DEPARTURE TIME 3:  "+departureLocalTime);



//        System.out.println("DEPARTURE TIME 2:  "+departureTime);
//        System.out.println("ARRIVAL TIME 2:  "+arrivalTime);
        UUID busUuid = UUID.fromString(busId);

        BusRootAndTime busRootAndTime = userService.addRootToBus(busUuid, routeTimeData);

        if (busRootAndTime != null) {
            System.out.println("success");
            return ResponseEntity.ok().body("{\"message\": \"Root added successfully\"}");
        } else {
            System.out.println("Failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"message\": \"Failed to add root\"}");
        }
    }

        public static LocalDate conversorStringToLocalDateTime(String convertDate) throws ParseException {
            LocalDate dateTime =LocalDate.parse(convertDate);
            return dateTime;
        }


    @GetMapping("/getBusRootById")
    public List<BusRootAndTime> findBusRootByBusId(@RequestParam("busId") String busId) {
        List<BusRootAndTime> busRoot = userService.findBusRootByBusId(busId);
        BusRootAndTime busRootData = busRoot.get(0);

        if (busRootData != null) {
            System.out.println("BUS ROOT " + busRootData);
            return busRoot;
        } else {
            System.out.println("BUS ROOT error " + busRootData);
            return busRoot;
        }
    }

    @GetMapping("/getBusRootByUuid")
    public BusRootAndTime findBusRootByBusUuid(@RequestParam("uuid") String uuid) {
        try {
            BusRootAndTime busRoot = userService.findBusRootByBusUuid(uuid);
            if (busRoot != null) {
                System.out.println("BUS ROUTE UUID: " + busRoot);
                return busRoot;
            } else {
                System.out.println("No bus route found for UUID: " + uuid);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Error fetching bus root and time: " + e.getMessage());
            throw e;
        }
    }

    @PostMapping("/add-more-details")
    public ResponseEntity<String> addMoreUserDetails(@RequestBody UserMoreDetailsRequest userMoreDetailsRequest) {
        System.out.println("h 1");
        userService.addMoreUserDetails(userMoreDetailsRequest);
        return ResponseEntity.ok("User details added successfully");
    }


    @PostMapping("/blockBus")
    public String blockUser(@RequestBody BusBlockRequest busBlockRequest) {
        try {

            userService.blockBus(busBlockRequest);
            return "bus blocked successfully in user service";
        } catch (Exception e) {
            return "Failed to block bus: " + e.getMessage();
        }
    }

    @PostMapping("/unblockBus")
    public String unblockUser(@RequestBody BusBlockRequest busBlockRequest) {
        try {
            userService.unblockBus(busBlockRequest);
            return "bus unblocked successfully  in user service";
        } catch (Exception e) {
            return "Failed to unblock bus: " + e.getMessage();
        }
    }

    @GetMapping("/getUserByMail")
    public UserEntity getUserByMail(@RequestParam String userMail) {
        UserEntity user = userService.getUserByEmail(userMail);
        return user;
    }


    @PostMapping("/updateWalletAndRetrieve")
    public ResponseEntity<?> updateWalletAndRetrieve(@RequestBody UpdateWalletRequest request) {
        try{
            double totalFare = request.getTotalFare();
            String email = request.getEmail();
            String bookingId = request.getBookingId();
            WalletEntity wallet = userService.updateWallet(totalFare, email, bookingId);
            return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
        }catch(DataIntegrityViolationException e){
            System.out.println("catch" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or contact already exists.");
        }

    }

    @PostMapping("/updateWalletAfterBooking")
    public ResponseEntity<?> updateWalletAfterBooking(@RequestBody UpdateWalletRequest request) {
        try{
            double totalFare = request.getTotalFare();
            String email = request.getEmail();
            String bookingId = request.getBookingId();
            WalletEntity wallet = userService.updateWalletAfterBooking(totalFare, email, bookingId);
            return ResponseEntity.status(HttpStatus.CREATED).body(wallet);
        }catch(DataIntegrityViolationException e){
            System.out.println("catch" + e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email or contact already exists.");
        }

    }

    @GetMapping("/getWallet")
    public ResponseEntity<?> getWalletByEmail(@RequestParam String email) {
        try {
            WalletEntity wallet = userService.getWalletByEmail(email);
            return ResponseEntity.ok(wallet);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to retrieve wallet");
        }
    }


    @GetMapping("/getUserWalletHistory")
    public List<WalletHistory> getUserWalletHistory(@RequestParam(required = false) String email) {
        System.out.println("getWalletHistory Email working "+ email);
        List <WalletHistory> walletHistory= userService.getALlUserWalletHistory(email);
        System.out.println("getWalletHistory Email Data "+ walletHistory);
        List<WalletHistory> filteredHistory = walletHistory.stream()
                .filter(entry -> entry.getWalletStatus() != null)
                .collect(Collectors.toList());

        System.out.println("getWalletHistory filteredHistory "+ filteredHistory);
        return  filteredHistory;
    }

}