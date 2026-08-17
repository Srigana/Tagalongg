package com.tagalong.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Payment is authorized (held) when a request is made, and captured only
 * when the poster accepts — mirroring the original "held, released on
 * acceptance" flow.
 */
@Service
public class StripeService {

    @Value("${stripe.api-key}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public String holdPayment(double amountUsd, String requesterEmail) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(Math.round(amountUsd * 100))
                .setCurrency("usd")
                .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL) // authorize now, capture later
                .putMetadata("requester_email", requesterEmail)
                .build();

        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getId();
    }

    public void capturePayment(String paymentIntentId) throws StripeException {
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
        intent.capture();
    }

    public void releasePayment(String paymentIntentId) throws StripeException {
        PaymentIntent intent = PaymentIntent.retrieve(paymentIntentId);
        intent.cancel();
    }
}
