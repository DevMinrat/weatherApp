package com.devminrat.weatherApp.controllers;


import com.devminrat.weatherApp.dto.AuthFormDTO;
import com.devminrat.weatherApp.models.Session;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.services.SessionService;
import com.devminrat.weatherApp.services.UserService;
import com.devminrat.weatherApp.utils.UserValidator;
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

import static com.devminrat.weatherApp.utils.UserValidator.*;

@Controller
@RequestMapping("/auth")
public class AuthController {
    private final UserValidator userValidator;
    @Value("${app.cookie.session}")
    private String cookieName;
    private final SessionService sessionService;
    private final UserService userService;

    @Autowired
    public AuthController(final SessionService sessionService, final UserService userService, UserValidator userValidator) {
        this.sessionService = sessionService;
        this.userService = userService;
        this.userValidator = userValidator;
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

        userValidator.validate(new User(authForm.getLogin(), authForm.getPassword()), bindingResult);

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("login")) {
                modelAndView.addObject(USER_ERROR, "Invalid login");
            }
            if (bindingResult.hasFieldErrors("password")) {
                modelAndView.addObject(PASSWORD_ERROR, "Invalid password");
            }
            return modelAndView;
        }

        Optional<User> existingUser = userService.findUserByLogin(authForm.getLogin());

        if (existingUser.isEmpty()) {
            modelAndView.addObject(USER_ERROR, "Invalid login");
            return modelAndView;
        }

        userValidator.validatePassword(existingUser.get(), authForm.getPassword(), bindingResult);

        if (bindingResult.hasErrors()) {
            modelAndView.addObject(PASSWORD_ERROR, "Invalid password");
            return modelAndView;
        }

        initSession(response, existingUser.get());

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

        User user = new User(authForm.getLogin(), authForm.getPassword());
        userValidator.validate(user, bindingResult);
        userValidator.validateLogin(user, bindingResult);

        if (bindingResult.hasErrors()) {
            if (bindingResult.hasFieldErrors("login")) {
                modelAndView.addObject(USER_NAME_ERROR, "Login already exists");
            }
            return modelAndView;
        }

        userService.save(user);
        initSession(response, user);

        return new ModelAndView("redirect:/weather");
    }

    private void initSession(HttpServletResponse response, User user) {
        Session session = sessionService.createSession(user);

        Cookie sessionCookie = new Cookie(cookieName, session.getSessionId());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setSecure(true);
        sessionCookie.setMaxAge(1800);
        response.addCookie(sessionCookie);
    }
}
