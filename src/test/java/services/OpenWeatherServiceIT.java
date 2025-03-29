package services;

import com.devminrat.weatherApp.config.TestConfig;
import com.devminrat.weatherApp.dto.CityDTO;
import com.devminrat.weatherApp.dto.WeatherResponseDTO;
import com.devminrat.weatherApp.services.OpenWeatherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("test")
public class OpenWeatherServiceIT {
    @Autowired
    private OpenWeatherService openWeatherService;

    @Test
    public void test_getWeatherForecast_London_byCity() {
        WeatherResponseDTO res = openWeatherService.getWeatherByCity("London").block();

        assertNotNull(res);
        assertEquals("London", res.getName());
        assertEquals(BigDecimal.valueOf(-0.1257), res.getCoord().getLon());
        assertEquals(BigDecimal.valueOf(51.5085), res.getCoord().getLat());
    }

    @Test
    public void test_getWeatherForecast_London_byCoordinates() {
        WeatherResponseDTO res = openWeatherService.getWeatherByCoordinates(BigDecimal.valueOf(51.5085), BigDecimal.valueOf(-0.1257)).block();

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
