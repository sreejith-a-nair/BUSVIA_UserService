//package com.busvia.userservice.controller;
//
//import com.busvia.userservice.entity.UserEntity;
//import com.busvia.userservice.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.util.Map;
//import java.util.Random;
//
//@Controller
//@RequestMapping("/otp")
//public class OtpController {
//    @Autowired
//    UserService userService;
//
//    @PostMapping("/generateOTPAndSendEmail")
//    public ResponseEntity<String> generateOTPAndSendEmail(Map<String, String> requestMap) {
//        try {
//            System.out.println("Email id "+requestMap.get("email"));
//            UserEntity userInfo = userService.findByEmail(requestMap.get("email"));
//            if (userInfo != null) {
//                String emailId = userInfo.getEmail();
//
//                int otp = generateRandom4DigitNumber();
//                String otpValue = String.valueOf(otp);
//                System.out.println("Generated OTP: " + otp);
//
//                String subject = "Reset Password - BusVia System";
//                String body = "Your OTP for password reset is: " + otp;
//                boolean emailSent = userService.sendMail(emailId, subject, body);
//
//                if (emailSent) {
//                    System.out.println("Email sent successfully!");
//                    return ResponseEntity.ok(otpValue); // Include OTP in response
//                } else {
//                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send email. Invalid email. Please enter a registered email.");
//                }
//            } else {
//                System.out.println("User with email " + requestMap.get("email") + " not found.");
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid email. Please enter a registered email.");
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send password reset email. Please try again later.");
//        }
//    }
//
//    private int generateRandom4DigitNumber() {
//        // Generate a random 4-digit number between 1000 and 9999
//        return new Random().nextInt(9000) + 1000;
//    }
//
//}
