package com.devminrat.weatherApp.services;

import com.devminrat.weatherApp.dto.WeatherResponseDTO;
import com.devminrat.weatherApp.models.Location;
import com.devminrat.weatherApp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WeatherService {
    OpenWeatherService openWeatherService;
    LocationService locationService;

    @Autowired
    public WeatherService(OpenWeatherService openWeatherService, LocationService locationService) {
        this.openWeatherService = openWeatherService;
        this.locationService = locationService;
    }

    public List<WeatherResponseDTO> getWeather(User user) {
        List<Location> locations = locationService.findAllByUser(user);
        List<WeatherResponseDTO> weatherResponseDTOs = new ArrayList<>();

        for (Location location : locations) {
            WeatherResponseDTO resp = openWeatherService.getWeatherByCoordinates(location.getLatitude(), location.getLongitude()).block();
            weatherResponseDTOs.add(resp);
        }

        return weatherResponseDTOs;
    }
}
