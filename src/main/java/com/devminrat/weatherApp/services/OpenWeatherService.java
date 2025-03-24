package com.devminrat.weatherApp.services;

import com.devminrat.weatherApp.dto.CityDTO;
import com.devminrat.weatherApp.dto.WeatherResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OpenWeatherService {
    private final WebClient webClient;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private static final String WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/weather";
    private static final String GEO_BASE_URL = "https://api.openweathermap.org/geo/1.0/direct";
    private static final int LIMIT = 10;

    @Autowired
    public OpenWeatherService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<WeatherResponseDTO> getWeatherByCity(String city) {
        String url = String.format("%s?q=%s&appid=%s", WEATHER_BASE_URL, city, apiKey);
        return webClient.get().uri(url).retrieve().bodyToMono(WeatherResponseDTO.class);
    }

    public Mono<WeatherResponseDTO> getWeatherByCoordinates(double lat, double lon) {
        String url = String.format("%s?lat=%s&lon=%s&appid=%s", WEATHER_BASE_URL, lat, lon, apiKey);
        return webClient.get().uri(url).retrieve().bodyToMono(WeatherResponseDTO.class);
    }

    public Flux<CityDTO> getCitiesByName(String city) {
        String url = String.format("%s?q=%s&limit=%s&appid=%s", GEO_BASE_URL, city, LIMIT, apiKey);
        return webClient.get().uri(url).retrieve().bodyToFlux(CityDTO.class).distinct(CityDTO::getCountry);
    }

}
