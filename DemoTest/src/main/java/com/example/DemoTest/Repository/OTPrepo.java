package com.example.DemoTest.Repository;

import com.example.DemoTest.Model.OTPvalidator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OTPrepo extends JpaRepository<OTPvalidator, Long> {

    OTPvalidator findByPhNumber(String phNumber);

}
