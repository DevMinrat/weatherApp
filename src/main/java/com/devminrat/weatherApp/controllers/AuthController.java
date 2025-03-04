package com.devminrat.weatherApp.controllers;


import com.devminrat.weatherApp.models.Session;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.repositories.UserRepository;
import com.devminrat.weatherApp.services.SessionService;
import com.devminrat.weatherApp.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String login() {
        return "auth/sign-in";
    }

    @PostMapping("/login")
    public String login(@RequestParam("login") String login,
                        @RequestParam("password") String password,
                        HttpServletResponse response) {

        User user = userService.findUserByLogin(login).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Wrong password");
        }

        Session session = sessionService.createSession(user);

        Cookie sessionCookie = new Cookie(cookieName, session.getSessionId());
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(1800);
        response.addCookie(sessionCookie);

        return "redirect:/";
    }

    @GetMapping("/registration")
    public String registration() {
        return "auth/sign-up";
    }
}
