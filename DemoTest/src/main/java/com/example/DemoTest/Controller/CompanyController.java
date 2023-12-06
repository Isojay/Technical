package com.example.DemoTest.Controller;

import com.example.DemoTest.DTO.CompanyRequest;
import com.example.DemoTest.DTO.CompanyResponse;
import com.example.DemoTest.Service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/company")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService service;

    @GetMapping
    public ResponseEntity<?> getDetails() {
        List<CompanyResponse> companies = service.findAll();
        if (companies.isEmpty()){
            return new ResponseEntity<>("No Companies", HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(companies, HttpStatus.OK);
    }

    @PostMapping("/secure")
    public ResponseEntity<String> saveDetails(@RequestBody CompanyRequest request, @RequestParam("document") MultipartFile file) throws IOException {
        String message = service.saveDetails(request, file);
        return new ResponseEntity<>(message, HttpStatus.CREATED);
    }

    @PutMapping("/secure")
    public ResponseEntity<String> editDetails(@RequestBody CompanyRequest request){
        String message = service.editDetails(request);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping("/secure/{id}")
    public ResponseEntity<String> deleteDetails(@PathVariable long id) throws Exception {
        String message = service.deleteDetails(id);
        return message.equals("Deletion Success") ? new ResponseEntity<>(message, HttpStatus.OK) : new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/secure/authTest")
    public String authTest(){
        return  "This is from Authenticated Test";
    }

}