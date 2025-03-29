package com.devminrat.weatherApp.services;

import com.devminrat.weatherApp.models.Location;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.repositories.LocationRepository;
import com.devminrat.weatherApp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Optional<Location> findById(int id) {
        return locationRepository.findById(id);
    }

    public List<Location> findAllByUser(User user) {
        return locationRepository.findAllByOwner(user);
    }

    @Transactional
    public Location save(Location location) {
        return locationRepository.save(location);
    }

    @Transactional
    public Location update(int id, Location location) {
        location.setId(id);
        return locationRepository.save(location);
    }

    @Transactional
    public void delete(int id) {
        locationRepository.deleteById(id);
    }

}
