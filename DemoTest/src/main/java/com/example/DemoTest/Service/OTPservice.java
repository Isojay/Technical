package com.example.DemoTest.Service;

import com.example.DemoTest.Model.OTPvalidator;
import com.example.DemoTest.Model.User;
import com.example.DemoTest.Repository.OTPrepo;
import com.example.DemoTest.Repository.UserRepo;
import com.example.DemoTest.Utils.OTPgenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OTPservice {

    private final OTPrepo oTPrepo;
    private final OTPgenerator oTPgenerator;
    private final UserRepo userRepo;

    public String generateAndSendOTP(String phoneNumber) throws Exception {

        String otp = oTPgenerator.generateRandomString();

        User user =userRepo.findByPhNumber(phoneNumber);

        if(user != null) {
            if (!user.getValidated()) {
                OTPvalidator otPvalidator = OTPvalidator
                        .builder()
                        .code(otp)
                        .phNumber(phoneNumber)
                        .expiryTime(LocalDateTime.now().plusMinutes(5))
                        .build();

                oTPrepo.save(otPvalidator);
                String message = "OTP is successfully sent to your Number \n" + createOTPMessage(phoneNumber, otp);
                return message;

            } else {
                return "Phone Number is Already Verified";
            }
        }
        else {
            throw new Exception("Invalid Phone number!!");
        }
    }

    private String createOTPMessage(String phoneNumber, String otp) {
        String sendTo = "To: " + phoneNumber + "\n";
        String message = "OTP: " + otp + " is your verification code to activate your Account\n";

        //Further Logic to send OTP;

        return sendTo + message;
    }

    public String validateOTP(String phoneNumber, String code) throws Exception {
        removeOTPS();
        OTPvalidator otpValidator = oTPrepo.findByPhNumber(phoneNumber);
        if (otpValidator == null) {
            return generateAndSendOTP(phoneNumber);
        } else if (otpValidator.getCode() != null && otpValidator.getCode().equals(code)) {
            User user = userRepo.findByPhNumber(phoneNumber);
            user.setValidated(true);
            userRepo.save(user);
            return "OTP is Successfully validated. Happy Logging in";
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid OTP. Try Again.");
        }
    }

    public void removeOTPS(){

        List<OTPvalidator> otps = oTPrepo.findAll();

        for (OTPvalidator otp : otps) {
           if (otp.getExpiryTime().isBefore( LocalDateTime.now())){
               oTPrepo.deleteById(otp.getId());
           }
        }

    }


}