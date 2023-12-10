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

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OTPservice {

    private final OTPrepo oTPrepo;
    private final OTPgenerator oTPgenerator;
    private final UserRepo userRepo;
    private final MessageService messageService;


    public String generateAndSendOTP(String phoneNumber, String using) throws Exception {

        String otp = oTPgenerator.generateRandomString();
        removeOTPS();
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
                return "OTP is successfully sent to your Number \n" + createOTPMessage(phoneNumber, otp, using);

            } else {
                return "Phone Number is Already Verified";
            }
        }
        else {
            throw new Exception("Invalid Phone number!!");
        }
    }

    private String createOTPMessage(String phoneNumber, String otp, String using) {

        String sendTo = "To: " + phoneNumber + "\n";
        String message1 = "OTP: " + otp + " is your verification code to activate your Account\n" + "This is a Test";

        String to = "977" + phoneNumber;

        if(Objects.equals(using, "Whatsapp")){
            messageService.sendWhatsAppMessage(to, message1);
        }else {

            messageService.sendOTPWithCarrier(to,message1);
        }

        return sendTo + message1;
    }

    public String validateOTP(String phoneNumber, String code) throws Exception {
        OTPvalidator otpValidator = oTPrepo.findByPhNumber(phoneNumber);
        if (otpValidator.getCode() != null && otpValidator.getCode().equals(code)) {
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