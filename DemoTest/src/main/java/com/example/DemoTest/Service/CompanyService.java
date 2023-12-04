package com.example.DemoTest.Service;


import com.example.DemoTest.DTO.CompanyRequest;
import com.example.DemoTest.DTO.CompanyResponse;
import com.example.DemoTest.Model.Company.CompanyDetails;
import com.example.DemoTest.Repository.CompanyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    public static String Uploaddir =  System.getProperty("user.dir")+"/src/main/resources/static";

    private final CompanyRepo repo;

    public List<CompanyResponse> findAll(){

        List<CompanyDetails> details = repo.findAll();
        List<CompanyResponse> responseList = new ArrayList<>();

        for (CompanyDetails detail : details) {
            CompanyResponse response = CompanyResponse
                    .builder()
                    .id(detail.getId())
                    .name(detail.getCName())
                    .location(detail.getCLocation())
                    .document(detail.getDocument())
                    .build();

            responseList.add(response);
        }

        return responseList;
    }

    public void saveDetails(CompanyRequest request , MultipartFile file) throws IOException {

        String docName;

        if(file.isEmpty()) {
            throw new IllegalArgumentException("File must not be empty");
        } else {
            docName = file.getOriginalFilename();
            Path filenameandPath = Paths.get(Uploaddir, docName);
            Files.write(filenameandPath, file.getBytes());
        }

        CompanyDetails details = CompanyDetails
                .builder()
                .cLocation(request.getLocation())
                .cName(request.getName())
                .document(docName)
                .build();

            repo.save(details);
    }

   public String editDetails(CompanyRequest companyRequest){

        Optional<CompanyDetails> detail = repo.findById(companyRequest.getId());

        StringBuilder message = new StringBuilder();

        if (detail.isPresent()){

            CompanyDetails details = detail.get();

            if (!Objects.equals(details.getCLocation(), companyRequest.getLocation())){
                details.setCLocation(companyRequest.getLocation());
                message.append("Location Changed.");
            }
           if (!Objects.equals(details.getCName(), companyRequest.getName())) {
               details.setCName(companyRequest.getName());
               message.append(" Company Name Changed");
           }

            repo.save(details);


        }
        return  message.toString();

    }

    public void deleteDetails(long id){
        repo.deleteById(id);
    }

}
