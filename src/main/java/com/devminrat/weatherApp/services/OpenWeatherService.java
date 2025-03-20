package com.devminrat.weatherApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenWeatherService {
    private final WebClient webClient;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    private static final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather";

    @Autowired
    public OpenWeatherService(WebClient webClient) {
        this.webClient = webClient;
    }

    public Mono<String> getWeatherByCity(String city) {
        String url = String.format("%s?q=%s&appid=%s", BASE_URL, city, apiKey);
        return webClient.get().uri(url).retrieve().bodyToMono(String.class);
    }

}
