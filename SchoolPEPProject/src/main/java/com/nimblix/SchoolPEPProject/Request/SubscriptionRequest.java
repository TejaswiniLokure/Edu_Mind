package com.nimblix.SchoolPEPProject.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SubscriptionRequest {
    private String planType;
    private Double amount;
    private String paymentRef;
}
