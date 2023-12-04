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
    public ResponseEntity<?> getDetails(){

        List<CompanyResponse> companies = service.findAll();


        if(companies.isEmpty()){

            return new ResponseEntity<>("No companies found.", HttpStatus.NO_CONTENT);
        }else{
            return new ResponseEntity<>(companies, HttpStatus.OK);
        }

    }

    @PostMapping
    public void saveDetails(@RequestBody CompanyRequest request, @RequestParam("document") MultipartFile file) throws IOException {
        service.saveDetails(request,file);
    }

    @PutMapping
    public String editDetails(@RequestBody CompanyRequest request){

       return service.editDetails(request);
    }

    @DeleteMapping("/{id}")
    public String deleteDetails(@PathVariable long id){

        try {
            service.deleteDetails(id);
            return "Deletion Success";
        }catch (Exception e){
            return e.getMessage();
        }

    }


}
