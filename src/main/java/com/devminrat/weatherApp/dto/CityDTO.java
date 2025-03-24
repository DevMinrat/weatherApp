package com.devminrat.weatherApp.dto;

import lombok.Data;

@Data
public class CityDTO {
    private String name;
    private String country;
    private String state;
    private double lat;
    private double lon;

}
