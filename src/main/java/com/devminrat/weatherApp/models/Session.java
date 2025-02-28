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

    @OneToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private User owner;

    @Column
    private LocalDateTime expiresAt;

}
