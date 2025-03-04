package com.devminrat.weatherApp.repositories;

import com.devminrat.weatherApp.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findBySessionId(String sessionId);

    void deleteBySessionId(String sessionId);

    @Query("SELECT s FROM Session s WHERE s.sessionId = :sessionId and s.expiresAt > :now")
    Optional<Session> findValidSession(String sessionId, LocalDateTime now);
}
