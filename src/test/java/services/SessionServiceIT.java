package services;

import com.devminrat.weatherApp.config.TestConfig;
import com.devminrat.weatherApp.models.Session;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.services.SessionService;
import com.devminrat.weatherApp.services.UserService;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TestConfig.class})
@ActiveProfiles("test")
public class SessionServiceIT {
    @Mock
    private static User user;

    @Autowired
    @InjectMocks
    private SessionService sessionService;

    @Autowired
    private Flyway flyway;

    @BeforeEach
    public void setUp() {
        flyway.clean();
        flyway.migrate();
    }

    @Test
    void test_createSession() {
        Session session = sessionService.createSession(user);

        Session savedSession = sessionService.findValidSession(session.getSessionId()).orElse(null);
        assertNotNull(savedSession);
        assertEquals(session.getSessionId(), savedSession.getSessionId());
    }

    @Test
    void test_expiredSession() {
        SessionService mockedSessionService = Mockito.spy(sessionService);

        Session expiredSession = new Session();
        expiredSession.setSessionId("sessionID");
        expiredSession.setOwner(user);
        expiredSession.setExpiresAt(LocalDateTime.MIN);

        when(mockedSessionService.createSession(user)).thenReturn(expiredSession);

        Session session = mockedSessionService.createSession(user);
        Session savedExpiredSession = mockedSessionService.findValidSession(session.getSessionId()).orElse(null);

        assertNull(savedExpiredSession);
    }

    @Test
    void test_deleteSession() {
        Session session = sessionService.createSession(user);
        Session savedSession = sessionService.findValidSession(session.getSessionId()).orElse(null);
        assertNotNull(savedSession);

        sessionService.delete(savedSession.getSessionId());
        Session deletedSession = sessionService.findValidSession(session.getSessionId()).orElse(null);
        assertNull(deletedSession);
    }
}
