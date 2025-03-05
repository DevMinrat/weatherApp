package com.devminrat.weatherApp.controllers;


import com.devminrat.weatherApp.dto.AuthFormDTO;
import com.devminrat.weatherApp.models.Session;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.repositories.UserRepository;
import com.devminrat.weatherApp.services.SessionService;
import com.devminrat.weatherApp.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
    public String login(Model model) {
        model.addAttribute("authForm", new AuthFormDTO());
        return "auth/sign-in";
    }

    @PostMapping("/login")
    public String login(@Valid AuthFormDTO formDTO,
                        BindingResult bindingResult,
                        HttpServletResponse response) {
        if (bindingResult.hasErrors())
            return "auth/sign-in";

        User user = userService.findUserByLogin(formDTO.getLogin()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(formDTO.getPassword())) {
            throw new RuntimeException("Wrong password");
        }

        Session session = sessionService.createSession(user);

        Cookie sessionCookie = new Cookie(cookieName, session.getSessionId());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(1800);
        response.addCookie(sessionCookie);

        return "redirect:/weather";
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("authForm", new AuthFormDTO());
        return "auth/sign-up";
    }
}
