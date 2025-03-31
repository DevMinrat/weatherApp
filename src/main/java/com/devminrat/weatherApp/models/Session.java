package com.devminrat.weatherApp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
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
    @ToString.Exclude
    private User owner;

    @Column(nullable = false)
    @ToString.Exclude
    private LocalDateTime expiresAt;

}
