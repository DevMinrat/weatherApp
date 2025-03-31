package com.devminrat.weatherApp.services;

import com.devminrat.weatherApp.dto.LocationWeatherDTO;
import com.devminrat.weatherApp.dto.WeatherResponseDTO;
import com.devminrat.weatherApp.models.Location;
import com.devminrat.weatherApp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    public Mono<List<LocationWeatherDTO>> getWeather(User user) {
        List<Location> locations = locationService.findAllByUser(user);

        return Flux.fromIterable(locations)
                .flatMap(l -> openWeatherService.getWeatherByCoordinates(l.getLatitude(), l.getLongitude())
                        .map(weather -> new LocationWeatherDTO(l.getId(), weather)))
                .collectList();
    }
}
