package services;

import com.devminrat.weatherApp.config.TestConfig;
import com.devminrat.weatherApp.dto.CityDTO;
import com.devminrat.weatherApp.dto.WeatherResponseDTO;
import com.devminrat.weatherApp.services.OpenWeatherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.Disposable;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("test")
public class OpenWeatherIT {
    @Autowired
    private OpenWeatherService openWeatherService;

    @Test
    public void test_getWeatherForecast_London_byCity() {
        WeatherResponseDTO res = openWeatherService.getWeatherByCity("London").block();

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

    @Test
    public void test_getWeatherForecast_London_byCityAndCoordinates() {
        List<CityDTO> cities = openWeatherService.getCitiesByName("london").toStream().toList();

        assertNotNull(cities);
        assertEquals(3, cities.size());
        assertEquals("London", cities.getFirst().getName());
        assertEquals("England", cities.getFirst().getState());
        assertEquals("GB", cities.getFirst().getCountry());

        assertEquals("London", cities.getLast().getName());
        assertEquals("Kentucky", cities.getLast().getState());
        assertEquals("US", cities.getLast().getCountry());
    }
}
