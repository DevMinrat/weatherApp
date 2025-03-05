package com.devminrat.weatherApp.services;

import com.devminrat.weatherApp.models.Session;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.repositories.SessionRepository;
import com.devminrat.weatherApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@DependsOn("entityManagerFactory")
@Transactional(readOnly = true)
public class SessionService {
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;

    @Autowired
    public SessionService(SessionRepository sessionRepository, UserRepository userRepository) {
        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    @Transactional
    public Session createSession(User user) {
        Session session = new Session();
        session.setSessionId(generateSessionId());
        session.setOwner(user);
        session.setExpiresAt(LocalDateTime.now().minusMinutes(30));

        return sessionRepository.save(session);
    }

    public Optional<Session> findValidSession(String sessionId) {
        return sessionRepository.findValidSession(sessionId, LocalDateTime.now());
    }

    @Transactional
    public void delete(String sessionId) {
        sessionRepository.deleteBySessionId(sessionId);
    }

}
