package com.example.DemoTest.Controller;

import com.example.DemoTest.DTO.AuthRequest;
import com.example.DemoTest.DTO.RegisterRequest;
import com.example.DemoTest.Service.UserService;
import com.example.DemoTest.Utils.OTPgenerator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {


    private final UserService userService;

    @PostConstruct
    public void adminEnter() throws Exception {

        if (userService.findByNumber("9808274990") == null){
            RegisterRequest request = RegisterRequest
                    .builder()
                    .name("Bijay Shrestha")
                    .phNumber("9808274990")
                    .email("isongum@gmail.com")
                    .password("Hello123")
                    .build();

            userService.registerUser(request);
        }
        OTPgenerator otPgenerator = new OTPgenerator();
        System.out.println(otPgenerator.generateRandomString());
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            System.out.println(registerRequest);
            userService.registerUser(registerRequest);
            return ResponseEntity.ok().body("User registration successful. A verification code is sent to your number please verify.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to register user. Error: " + e.getMessage());
        }
    }
    @GetMapping("/logIn")
    public ResponseEntity<?> authUser(@RequestBody AuthRequest request){
        try {
            System.out.println(request);
            if(userService.authenticate(request)){
                return new ResponseEntity<>("Log In Successful", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Mobile Verification Required. Please validate your login details and try again.", HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>("An error occurred while processing your request. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/sendOTP")
    public String oj(){
        return "OTP Sent";
    }

    @PostMapping
    public void validateOTP(){

    }

}