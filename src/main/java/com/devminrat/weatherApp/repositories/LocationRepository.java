package com.devminrat.weatherApp.repositories;

import com.devminrat.weatherApp.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Session, Integer> {

}
