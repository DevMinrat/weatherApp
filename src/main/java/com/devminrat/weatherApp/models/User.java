package com.devminrat.weatherApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Login should not be empty")
    @Size(min = 2, max = 30, message = "Login length should be between 2 and 30")
    @Column
    private String login;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 2, max = 30, message = "Password length should be between 2 and 30")
    @Column
    private String password;

    @OneToMany(mappedBy = "owner")
    private List<Location> locations;

    @OneToMany(mappedBy = "owner")
    private List<Session> session;
}
