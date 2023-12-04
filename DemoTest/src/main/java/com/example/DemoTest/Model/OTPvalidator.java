package com.example.DemoTest.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "OTPvalidator")
public class OTPvalidator {

   @Id
   @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private long Id;

   @Column(name = "verificationCode")
   private String code;

   @Column(name= "phoneNumber")
   private String phNumber;

   @Column(name = "expireTime")
   private LocalDateTime expiryTime;



}
