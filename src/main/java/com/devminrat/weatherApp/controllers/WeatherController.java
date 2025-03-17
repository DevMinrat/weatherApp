package com.devminrat.weatherApp.controllers;

import com.devminrat.weatherApp.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/weather")
public class WeatherController {

    @GetMapping
    public ModelAndView homePage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("weather/index");

        User user = (User) request.getAttribute("currentUser");
        modelAndView.addObject("userLogin", user.getLogin());

        return modelAndView;
    }

    @GetMapping("/test")
    public String test() {
        return "weather/search-results";
    }
}
