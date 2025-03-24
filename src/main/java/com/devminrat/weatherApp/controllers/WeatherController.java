package com.devminrat.weatherApp.controllers;

import com.devminrat.weatherApp.dto.CityDTO;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.services.OpenWeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private OpenWeatherService openWeatherService;

    @Autowired
    public WeatherController(OpenWeatherService openWeatherService) {
        this.openWeatherService = openWeatherService;
    }

    @GetMapping
    public ModelAndView homePage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("weather/index");

        User user = (User) request.getAttribute("currentUser");
        modelAndView.addObject("userLogin", user.getLogin());

        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView search() {

        return new ModelAndView("weather/search");
    }

    @PostMapping("/search")
    public ModelAndView search(@RequestParam String city, Model model) {
        List<CityDTO> cities = openWeatherService.getCitiesByName(city).toStream().toList();

        model.addAttribute("cities", cities);
        model.addAttribute("city", city);
        return new ModelAndView("weather/search");
    }
}
