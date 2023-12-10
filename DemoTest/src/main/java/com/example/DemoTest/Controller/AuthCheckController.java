package com.example.DemoTest.Controller;

import com.example.DemoTest.JWT.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class AuthCheckController {

    private final JwtService jwtService;
    @GetMapping("/public")
    public String getHello(){
        return  "This is from Public API";
    }

    @GetMapping("/secure")
    public String getAuthHello(@RequestHeader("Authorization") String token) throws Exception {

        String userName = getJWT(token);
        log.info("Logged User : {}", userName);
        return  "This is from Authenticated API";

    }

    public String getJWT(String token) throws Exception {
        String jwt = token.substring(7);
        String userName = jwtService.extractUsername(jwt);
        System.out.println(userName);
        return userName;
    }

}
