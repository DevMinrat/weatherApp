package com.devminrat.weatherApp.utils;

import com.devminrat.weatherApp.models.Session;
import com.devminrat.weatherApp.services.SessionService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Optional;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Value("${app.cookie.session}")
    private String cookieName;
    private final SessionService sessionService;

    @Autowired
    public SessionInterceptor(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            Optional<Cookie> sessionCookie = Arrays.stream(cookies).filter(c -> cookieName.equals(c.getName())).findFirst();

            if (sessionCookie.isPresent()) {
                String sessionId = sessionCookie.get().getValue();
                Optional<Session> session = sessionService.findValidSession(sessionId);

                session.ifPresent(s -> request.setAttribute("currentUser", s.getOwner()));
                return true;
            } else {
                Cookie cookie = new Cookie(cookieName, "");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        response.sendRedirect("/auth/sign-in");
        return false;
    }

}
