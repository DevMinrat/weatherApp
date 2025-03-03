package com.devminrat.weatherApp.repositories;

import com.devminrat.weatherApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
