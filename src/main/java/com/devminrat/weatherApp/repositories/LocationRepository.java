package com.devminrat.weatherApp.repositories;

import com.devminrat.weatherApp.models.Location;
import com.devminrat.weatherApp.models.Session;
import com.devminrat.weatherApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface LocationRepository extends JpaRepository<Location, Integer> {
    Optional<Location> findByName(String name);
    List<Location> findAllByOwner(User user);

    @Transactional
    void deleteById(int id);
}
