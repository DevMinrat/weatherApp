package com.devminrat.weatherApp.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column
    private String login;

    @Column
    private String password;

    @OneToMany(mappedBy = "owner")
    @ToString.Exclude
    private List<Location> locations;

    @OneToMany(mappedBy = "owner", orphanRemoval = true)
    @ToString.Exclude
    private List<Session> session;
}
