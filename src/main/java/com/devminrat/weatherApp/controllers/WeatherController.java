package com.devminrat.weatherApp.controllers;

import com.devminrat.weatherApp.dto.CityDTO;
import com.devminrat.weatherApp.dto.LocationDTO;
import com.devminrat.weatherApp.dto.LocationWeatherDTO;
import com.devminrat.weatherApp.dto.WeatherResponseDTO;
import com.devminrat.weatherApp.models.Location;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.services.LocationService;
import com.devminrat.weatherApp.services.OpenWeatherService;
import com.devminrat.weatherApp.services.WeatherService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    private final LocationService locationService;
    private final OpenWeatherService openWeatherService;
    private final WeatherService weatherService;

    @Autowired
    public WeatherController(OpenWeatherService openWeatherService, LocationService locationService, WeatherService weatherService) {
        this.openWeatherService = openWeatherService;
        this.locationService = locationService;
        this.weatherService = weatherService;
    }

    @GetMapping
    public ModelAndView homePage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("weather/index");

        User user = (User) request.getAttribute("currentUser");
        modelAndView.addObject("userLogin", user.getLogin());

        List<LocationWeatherDTO> weatherList = weatherService.getWeather(user).block();

        modelAndView.addObject("weatherList", weatherList);

        return modelAndView;
    }

    @GetMapping("/search")
    public ModelAndView search(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("weather/search");

        User user = (User) request.getAttribute("currentUser");
        modelAndView.addObject("userLogin", user.getLogin());

        return modelAndView;
    }

    @PostMapping("/search")
    public ModelAndView search(@RequestParam String city, HttpServletRequest request, Model model) {
        ModelAndView modelAndView = new ModelAndView("weather/search");

        User user = (User) request.getAttribute("currentUser");
        modelAndView.addObject("userLogin", user.getLogin());

        List<CityDTO> cities = openWeatherService.getCitiesByName(city).toStream().toList();

        model.addAttribute("cities", cities);
        model.addAttribute("city", city);
        return modelAndView;
    }

    @PostMapping("/search/add")
    public ModelAndView addLocation(@ModelAttribute("locationItemForm") LocationDTO locationItemForm, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");

        Location location = new Location();
        location.setOwner(user);
        location.setName(locationItemForm.getName());
        location.setLongitude(locationItemForm.getLon());
        location.setLatitude(locationItemForm.getLat());

        locationService.save(location);

        return new ModelAndView("redirect:/weather");
    }

    @DeleteMapping("/delete")
    public ModelAndView delete(@RequestParam int id, HttpServletRequest request) {
        User user = (User) request.getAttribute("currentUser");
        Optional<Location> location = locationService.findById(id);

        if (location.isPresent() && location.get().getOwner().equals(user)) {
            locationService.delete(id);
        } else {
            return new ModelAndView("redirect:/weather");
        }

        return new ModelAndView("redirect:/weather");
    }
}
