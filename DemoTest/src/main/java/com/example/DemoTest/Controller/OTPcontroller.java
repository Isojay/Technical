package com.example.DemoTest.Controller;

import com.example.DemoTest.Service.OTPservice;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/otp")
@Slf4j
public class OTPcontroller {

    private final OTPservice otPservice;

    @GetMapping("/generateOTP")
    public ResponseEntity<String> generateOTP(@RequestParam String phNumber, @RequestParam String using){
        log.info(using);
        try{
            String message = otPservice.generateAndSendOTP(phNumber, using);
            return new ResponseEntity<>(message, HttpStatus.OK);

        }catch (Exception e){
            log.error("Error while generating OTP : {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/validateOTP")
    public ResponseEntity<?> validateOTP(String phNumber, String OTP){

        try{
            String message = otPservice.validateOTP(phNumber,OTP);
            return new ResponseEntity<>(message, HttpStatus.OK);

        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }
}