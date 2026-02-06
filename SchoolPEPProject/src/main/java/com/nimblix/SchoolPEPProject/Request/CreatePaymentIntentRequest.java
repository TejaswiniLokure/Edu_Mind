package com.nimblix.SchoolPEPProject.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentIntentRequest {

    private String planType;  // MONTHLY or YEARLY
    private Double amount;    // in INR or your currency
    private String currency; // e.g. "inr", "usd" (default inr)
}
