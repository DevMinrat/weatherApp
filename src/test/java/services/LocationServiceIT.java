package services;

import com.devminrat.weatherApp.config.TestConfig;
import com.devminrat.weatherApp.models.Location;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.repositories.UserRepository;
import com.devminrat.weatherApp.services.LocationService;
import com.devminrat.weatherApp.services.UserService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("test")
public class LocationServiceIT {

    private User user;

    @Autowired
    private LocationService locationService;
    @Autowired
    private Flyway flyway;

    private final Location location1 = new Location();
    private final Location location2 = new Location();

    private static final BigDecimal LATITUDE = BigDecimal.valueOf(51.507321);
    private static final BigDecimal LONGITUDE = BigDecimal.valueOf(-0.127647);
    private static final String LOC_NAME = "London";
    @Autowired
    private UserService userService;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setLogin("User");
        user.setPassword("12345");

        location1.setLatitude(LATITUDE);
        location1.setLongitude(LONGITUDE);
        location1.setName(LOC_NAME);

        location2.setLatitude(BigDecimal.valueOf(52.5200));
        location2.setLongitude(BigDecimal.valueOf(13.4050));
        location2.setName("Berlin");

        flyway.clean();
        flyway.migrate();
    }

    @Test
    void test_locationSave() {
        User savedUser = userService.save(user);

        location1.setOwner(savedUser);
        locationService.save(location1);
        Optional<Location> savedLocation = locationService.findById(1);

        assertTrue(savedLocation.isPresent());
        assertEquals(LOC_NAME, savedLocation.get().getName());
        assertEquals(LATITUDE, savedLocation.get().getLatitude());
        assertEquals(LONGITUDE, savedLocation.get().getLongitude());
    }

    @Test
    void test_findAllLocationsByUser() {
        User savedUser = userService.save(user);

        location1.setOwner(savedUser);
        location2.setOwner(savedUser);
        locationService.save(location1);
        locationService.save(location2);

        List<Location> savedLocation = locationService.findAllByUser(user);
        assertNotNull(savedLocation);
        assertEquals(2, savedLocation.size());
        assertEquals(LOC_NAME, savedLocation.getFirst().getName());
        assertEquals(LATITUDE, savedLocation.getFirst().getLatitude());
        assertEquals(LONGITUDE, savedLocation.getFirst().getLongitude());
    }

}
