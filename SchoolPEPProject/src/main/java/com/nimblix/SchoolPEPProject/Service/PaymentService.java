package com.nimblix.SchoolPEPProject.Service;

import com.nimblix.SchoolPEPProject.Model.School;
import com.nimblix.SchoolPEPProject.Request.CreatePaymentIntentRequest;
import com.nimblix.SchoolPEPProject.Response.PaymentIntentResponse;

public interface PaymentService {

    /**
     * Creates a Stripe PaymentIntent for subscription. Caller must be authenticated school.
     */
    PaymentIntentResponse createPaymentIntent(School school, CreatePaymentIntentRequest request);

    /**
     * Handles Stripe webhook (e.g. payment_intent.succeeded) to activate subscription.
     */
    void handleStripeWebhook(String payload, String signature);
}
