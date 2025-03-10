package com.devminrat.weatherApp.controllers;


import com.devminrat.weatherApp.dto.AuthFormDTO;
import com.devminrat.weatherApp.models.Session;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.services.SessionService;
import com.devminrat.weatherApp.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {
    @Value("${app.cookie.session}")
    private String cookieName;
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public AuthController(final SessionService sessionService, final UserService userService) {
        this.sessionService = sessionService;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String login(@ModelAttribute("authForm") AuthFormDTO authForm) {
        return "auth/sign-in";
    }

    @PostMapping("/login")
    public ModelAndView login(@ModelAttribute("authForm") @Valid AuthFormDTO authForm,
                              BindingResult bindingResult,
                              HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("auth/sign-in");

        if (bindingResult.hasErrors())
            return modelAndView;

        Optional<User> user = userService.findUserByLogin(authForm.getLogin());

        if (user.isEmpty()) {
            modelAndView.addObject("userError", "Invalid login");
            return modelAndView;
        }

        if (!user.get().getPassword().equals(authForm.getPassword())) {
            modelAndView.addObject("passwordError", "Invalid password");
            return modelAndView;
        }

        initSession(response, user.get());

        return new ModelAndView("redirect:/weather");
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("authForm") AuthFormDTO authForm) {
        return "auth/sign-up";
    }

    @PostMapping("/registration")
    public ModelAndView registration(@ModelAttribute("authForm") @Valid AuthFormDTO authForm,
                                     BindingResult bindingResult,
                                     HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("auth/sign-up");

        if (bindingResult.hasErrors())
            return modelAndView;

        boolean isLoginExist = userService.findUserByLogin(authForm.getLogin()).isPresent();

        if (isLoginExist) {
            modelAndView.addObject("userNameError", "Login already exists");
            return modelAndView;
        }

        User user = new User(authForm.getLogin(), authForm.getPassword());

        userService.save(user);
        initSession(response, user);

        return new ModelAndView("redirect:/weather");
    }

    private void initSession(HttpServletResponse response, User user) {
        Session session = sessionService.createSession(user);

        Cookie sessionCookie = new Cookie(cookieName, session.getSessionId());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(1800);
        response.addCookie(sessionCookie);
    }
}
