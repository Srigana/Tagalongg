package com.tagalong.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
public class Post {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "poster_id", nullable = false)
    private User poster;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private Instant departureTime;

    @Column(nullable = false)
    private Integer availableSlots;

    @Column(nullable = false)
    private Double farePerSeat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PostStatus status = PostStatus.ACTIVE;

    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    public enum PostStatus {
        ACTIVE, FULL, CANCELLED, COMPLETED
    }
}
