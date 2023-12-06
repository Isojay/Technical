package com.example.DemoTest.Service;

import com.example.DemoTest.Model.OTPvalidator;
import com.example.DemoTest.Model.User;
import com.example.DemoTest.Repository.OTPrepo;
import com.example.DemoTest.Repository.UserRepo;
import com.example.DemoTest.Utils.OTPgenerator;
import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class OTPservice {

    private final OTPrepo oTPrepo;
    private final OTPgenerator oTPgenerator;
    private final UserRepo userRepo;

    @Value("${vonage.api.key}")
    private String API_KEY;

    @Value("${vonage.api.secret}")
    private String API_SECRET;

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
        String message1 = "OTP: " + otp + " is your verification code to activate your Account\n";

        String to = "977" + phoneNumber;

        if(Objects.equals(using, "Whatsapp")){
            sendWhatsAppMessage(to, message1);
        }else {
            sendOTPWithCarrier(to,message1);
        }

        return sendTo + message1;
    }

    public void sendOTPWithCarrier(String to, String message1){

        VonageClient client = VonageClient.builder().apiKey(API_KEY).apiSecret(API_SECRET).build();

        TextMessage message = new TextMessage("Mobile Verification", to,
                message1);

        SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            System.out.println("Message sent successfully.");
        } else {
            System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
        }
    }


    public void sendWhatsAppMessage(String to, String Message) {

        String url = "https://messages-sandbox.nexmo.com/v1/messages";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setBasicAuth(API_KEY, API_SECRET);

        // Prepare payload
        Map<String, String> payload = new HashMap<>();
        payload.put("from", "14157386102");
        payload.put("to", to);
        payload.put("message_type", "text");
        payload.put("text", Message);
        payload.put("channel", "whatsapp");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(payload, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        // Print the response status code and body
        System.out.println("Response status code: " + response.getStatusCode());
        System.out.println("Response body: " + response.getBody());
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