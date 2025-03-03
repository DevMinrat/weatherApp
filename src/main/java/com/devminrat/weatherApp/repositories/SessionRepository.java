package com.devminrat.weatherApp.repositories;

import com.devminrat.weatherApp.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Integer> {
}
