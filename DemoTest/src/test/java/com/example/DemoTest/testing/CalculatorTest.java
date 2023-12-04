package com.example.DemoTest.testing;

import com.example.DemoTest.DTO.CompanyRequest;
import com.example.DemoTest.Model.Company.CompanyDetails;
import com.example.DemoTest.Repository.CompanyRepo;
import com.example.DemoTest.Service.CompanyService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CalculatorTest {
    @InjectMocks
    private CompanyService service;

    @Mock
    private CompanyRequest request;

    @Mock
    private MultipartFile file;

    @Mock
    private CompanyRepo repo;


    @Test
    public void testSaveDetails() throws IOException {
        // Arrange
        String companyName = "Test Company";
        String cLocation = "Test Location";
        String docName = "Test Doc";
        byte[] bytes = "Test Content".getBytes();

        when(request.getName()).thenReturn(companyName);
        when(request.getLocation()).thenReturn(cLocation);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn(docName);
        when(file.getBytes()).thenReturn(bytes);

        CompanyDetails companyDetails = CompanyDetails.builder()
                .location(cLocation)
                .name(companyName)
                .document(docName)
                .build();

        when(repo.save(any(CompanyDetails.class))).thenReturn(companyDetails);

        // Act
        service.saveDetails(request, file);

        // Assert
        verify(request, times(1)).getName();
        verify(request, times(1)).getLocation();

        verify(file, times(1)).isEmpty();
        verify(file, times(1)).getOriginalFilename();
        verify(file, times(1)).getBytes();

        ArgumentCaptor<CompanyDetails> captor = ArgumentCaptor.forClass(CompanyDetails.class);
        verify(repo, times(1)).save(captor.capture());

        CompanyDetails saved = captor.getValue();
        assertEquals(companyName, saved.getName());
        assertEquals(cLocation, saved.getLocation());
        assertEquals(docName, saved.getDocument());
    }

}