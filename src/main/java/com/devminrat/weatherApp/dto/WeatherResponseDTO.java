package com.devminrat.weatherApp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WeatherResponseDTO {
    private Coord coord;
    private Weather[] weather;
    private Main main;
    private String name;

    @Data
    @ToString
    public static class Coord {
        private double lon;
        private double lat;
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
}
