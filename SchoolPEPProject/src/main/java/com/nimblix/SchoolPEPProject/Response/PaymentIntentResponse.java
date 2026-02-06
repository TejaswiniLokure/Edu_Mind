package com.nimblix.SchoolPEPProject.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentIntentResponse {

    // For Razorpay: this is the public key id needed by frontend checkout
    private String keyId;
    // For Razorpay: this is the created order id (order_xxx)
    private String orderId;
    private Long amount;
    private String currency;
}
