package com.example.DemoTest.Repository;

import com.example.DemoTest.Model.Company.CompanyDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepo extends JpaRepository<CompanyDetails,Long> {

    CompanyDetails findByCNameIgnoreCase(String name);

}
