package com.example.DemoTest.Service;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public class MessageService {
    @Value("${vonage.api.key}")
    private String API_KEY;

    @Value("${vonage.api.secret}")
    private String API_SECRET;



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

        //Only works if number is registered in the Vonage Sandbox;

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
}