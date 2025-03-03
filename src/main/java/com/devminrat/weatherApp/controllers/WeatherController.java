package com.devminrat.weatherApp.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    @GetMapping
    public String homePage() {
        return "weather/index";
    }

    @GetMapping("/test")
    public String test() {
        return "weather/search-results";
    }
}
