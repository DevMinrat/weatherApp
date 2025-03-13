package services;

import com.devminrat.weatherApp.config.TestConfig;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.services.UserService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("test")
public class UserServiceIT {
    @Autowired
    private UserService userService;
    @Autowired
    private Flyway flyway;

    private static final String userLogin = "testLogin";
    private static final String userPassword = "12345";

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void test_userRegistration() {
        User user = new User(userLogin, userPassword);
        userService.save(user);

        User savedUser = userService.findUserByLogin(user.getLogin()).orElse(null);
        assertNotNull(savedUser);
        assertEquals(userLogin, savedUser.getLogin());
    }

    @Test
    void test_userAlreadyRegistered() {
        User firstUser = new User(userLogin, userPassword);
        User secondUser = new User(userLogin, userPassword);
        userService.save(firstUser);

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> userService.save(secondUser));

        assertTrue(exception.getMessage().contains("Unique index or primary key violation"));
    }

    @Test
    void test_userNotFound() {
        User user = new User(userLogin, userPassword);
        userService.save(user);
        User savedUser = userService.findUserByLogin("differentLogin").orElse(null);

        assertNull(savedUser);
    }

    @Test
    void test_checkPassword() {
        User user = new User(userLogin, userPassword);
        userService.save(user);
        User savedUser = userService.findUserByLogin(user.getLogin()).get();

        assertTrue(userService.checkPassword(userPassword, savedUser.getPassword()));
    }
}
