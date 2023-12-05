package com.example.DemoTest.Service;

import com.example.DemoTest.DTO.AuthRequest;
import com.example.DemoTest.DTO.RegisterRequest;
import com.example.DemoTest.JWT.JwtService;
import com.example.DemoTest.Model.Role;
import com.example.DemoTest.Model.User;
import com.example.DemoTest.Repository.UserRepo;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public User findByNumber(String number){
        return userRepo.findByPhNumber(number);
    }

    public void registerUser(RegisterRequest registerRequest) throws Exception {

            String regexStr = "^[0-9]{10}$";

            // Check if phone number is valid
            if(!registerRequest.getPhNumber().matches(regexStr)) {
                throw new Exception("Invalid phone number");
            }

            if (userRepo.findByPhNumber(registerRequest.getPhNumber()) == null) {
                User user = User.builder()
                        .uEmail(registerRequest.getEmail())
                        .uPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()))
                        .phNumber(registerRequest.getPhNumber())
                        .validated(false)
                        .role(Role.USER)
                        .build();
                userRepo.save(user);
            } else {
                throw new Exception("User Already Registered");
            }


    }

    public String authenticate(AuthRequest request){

        if(findByNumber(request.getPhNumber()) != null) {

            if (authenticateUser(request) && isUserEnabled(request.getPhNumber())){

                User user = findByNumber(request.getPhNumber());
                return jwtService.generateToken(user);

            }else {
                return null;
            }

        }else {
            throw new BadCredentialsException("User Credential Not found");
        }
    }

    private boolean authenticateUser(AuthRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getPhNumber(), request.getUPassword()));

            return true;
        }catch (BadCredentialsException e){
            System.out.println(e.getMessage());
            return false;
        }
    }

    private Boolean isUserEnabled(String phoneNumber) {
        User user = userRepo.findByPhNumber(phoneNumber);
        return user != null && user.getValidated();
    }
}