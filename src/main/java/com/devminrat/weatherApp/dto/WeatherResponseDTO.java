package com.devminrat.weatherApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class WeatherResponseDTO {
    private int id;
    private Coord coord;
    private Weather[] weather;
    private Main main;
    private String name;
    private Sys sys;

    @Data
    @ToString
    public static class Coord {
        private BigDecimal lon;
        private BigDecimal lat;
    }

    @Data
    @ToString
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;
    }

    @Data
    @ToString
    public static class Main {
        private double temp;
        @JsonProperty("feels_like")
        private double feelsLike;
        @JsonProperty("temp_min")
        private double tempMin;
        @JsonProperty("temp_max")
        private double tempMax;
        private int pressure;
        private int humidity;
    }

    @Data
    @ToString
    public static class Sys {
        private String country;
    }
}
