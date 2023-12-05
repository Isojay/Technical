package com.example.DemoTest.Controller;

import com.example.DemoTest.DTO.AuthRequest;
import com.example.DemoTest.DTO.RegisterRequest;
import com.example.DemoTest.Service.UserService;
import com.example.DemoTest.Utils.OTPgenerator;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        try {
            userService.registerUser(registerRequest);
            return ResponseEntity.ok().body("User registration successful. \nPlease verify phone Number to get access of your Account");
        } catch (Exception e) {
            return new ResponseEntity<>("Unable to register user. Error: " + e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
    @GetMapping("/logIn")
    public ResponseEntity<?> authUser(@RequestBody AuthRequest request){
        try {
            String token = userService.authenticate(request);
            if(userService.authenticate(request)!= null){
                return new ResponseEntity<>("Log In Successful \n Your JWT Token : "+ token, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Mobile Verification Required. Please validate your login details and try again.", HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException e){
            System.out.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>("An error occurred while processing your request. Please try again later.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/secure")
    public String getHello(){
        return  "This is from Authenticated Test";
    }

}