package services;

import com.devminrat.weatherApp.config.TestConfig;
import com.devminrat.weatherApp.dto.WeatherResponseDTO;
import com.devminrat.weatherApp.models.Location;
import com.devminrat.weatherApp.services.OpenWeatherService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("test")
public class OpenWeatherIT {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private OpenWeatherService openWeatherService;
    @Autowired
    private Flyway flyway;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    public void test_getWeatherForecast_London_byCity() {
        WeatherResponseDTO res = openWeatherService.getWeatherByCity("London").block();

        System.out.println(res);
        assertNotNull(res);
        assertEquals("London", res.getName());
        assertEquals(-0.1257, res.getCoord().getLon());
        assertEquals(51.5085, res.getCoord().getLat());
    }

    @Test
    public void test_getWeatherForecast_London_byCoordinates() {
        WeatherResponseDTO res = openWeatherService.getWeatherByCoordinates(51.5085, -0.1257).block();

        assertNotNull(res);
        assertEquals("London", res.getName());
    }
}
