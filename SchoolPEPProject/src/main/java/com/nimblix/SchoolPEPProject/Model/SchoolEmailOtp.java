package com.nimblix.SchoolPEPProject.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "school_email_otp")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolEmailOtp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "otp")
    private String otp;

    @Column(name = "expiry_time")
    private String expiryTime;

    @Column(name = "verified")
    private Boolean verified;
}
