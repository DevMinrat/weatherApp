package com.devminrat.weatherApp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "locations")
public class Location {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name length should be between 2 and 30")
    @Column
    private String name;

    @ManyToOne
    @JoinColumn(name = "userid", referencedColumnName = "id")
    private User owner;

    @NotEmpty(message = "latitude should not be empty")
    @Column
    private double latitude;

    @NotEmpty(message = "longitude should not be empty")
    @Column
    private double longitude;
}
