package com.devminrat.weatherApp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LocationWeatherDTO {
    private int locationId;
    private WeatherResponseDTO weatherResp;
}
