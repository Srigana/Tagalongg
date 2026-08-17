package com.tagalong.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
public class RideRequest {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status = RequestStatus.PENDING;

    @Column(nullable = false)
    private Double fareAmount;

    /** Stripe PaymentIntent id — funds held on request, captured on acceptance */
    private String stripePaymentIntentId;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public enum RequestStatus {
        PENDING, ACCEPTED, REJECTED, CANCELLED, COMPLETED
    }
}
