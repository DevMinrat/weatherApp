package com.devminrat.weatherApp.models;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "sessions")
public class Session {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "session_id", nullable = false, unique = true)
    private String sessionId;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private User owner;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

}
