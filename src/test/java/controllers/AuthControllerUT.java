package controllers;

import com.devminrat.weatherApp.controllers.AuthController;
import com.devminrat.weatherApp.dto.AuthFormDTO;
import com.devminrat.weatherApp.models.Session;
import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.services.SessionService;
import com.devminrat.weatherApp.services.UserService;
import com.devminrat.weatherApp.utils.UserValidator;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUT {
    @Mock
    private UserValidator userValidator;
    @Mock
    private SessionService sessionService;
    @Mock
    private UserService userService;

    @InjectMocks
    private AuthController authController;

    private AuthFormDTO authForm;
    private BindingResult bindingResult;
    private MockHttpServletResponse response;

    private String cookieName;

    @BeforeEach
    public void setUp() {
        authForm = new AuthFormDTO();
        authForm.setLogin("validLogin");
        authForm.setPassword("validPassword");
        bindingResult = new BeanPropertyBindingResult(authForm, "authForm");
        response = new MockHttpServletResponse();

        cookieName = (String) ReflectionTestUtils.getField(authController, "COOKIE_NAME");
    }

    @Test
    public void test_ValidCredentials_ShouldRedirectToWeather() {
        User user = new User("validLogin", "validPassword");
        user.setId(1);
        Session session = new Session();
        session.setOwner(user);
        session.setSessionId("sessionId");

        when(userService.findUserByLogin("validLogin")).thenReturn(Optional.of(user));
        when(sessionService.createSession(user)).thenReturn(session);
        ModelAndView mav = authController.login(authForm, bindingResult, response);

        assertEquals("redirect:/weather", mav.getViewName());
        assertCookieIsSet(response);
    }

    private void assertCookieIsSet(MockHttpServletResponse response) {
        Cookie cookie = response.getCookie(cookieName);
        assertNotNull(cookie);
        assertEquals("/", cookie.getPath());
        assertTrue(cookie.isHttpOnly());
        assertEquals(1800, cookie.getMaxAge());
    }

    @Test
    public void test_InvalidPassword_ShouldRedirectToLogin() {
        User user = new User("validLogin", "invalidPassword");

        when(userService.findUserByLogin("validLogin")).thenReturn(Optional.of(user));
        doAnswer(invocation -> {
            bindingResult.addError(new ObjectError("authForm", "invalidPassword"));
            return null;
        }).when(userValidator).validatePassword(user, authForm.getPassword(), bindingResult);
        ModelAndView mav = authController.login(authForm, bindingResult, response);

        assertTrue(bindingResult.hasErrors());
        assertEquals("invalidPassword", bindingResult.getAllErrors().getFirst().getDefaultMessage());
        assertEquals("auth/sign-in", mav.getViewName());
    }

    @Test
    public void test_InvalidLogin_ShouldRedirectToLogin() {
        when(userService.findUserByLogin("validLogin")).then(inv -> {
            bindingResult.addError(new ObjectError("authForm", "invalidLogin"));
            return Optional.empty();
        });
        ModelAndView mav = authController.login(authForm, bindingResult, response);

        assertTrue(bindingResult.hasErrors());
        assertEquals("invalidLogin", bindingResult.getAllErrors().getFirst().getDefaultMessage());
        assertEquals("auth/sign-in", mav.getViewName());
    }

    @Test
    void test_registration_NewUser_ShouldCreateUserAndRedirect() {
        when(sessionService.createSession(any(User.class))).thenReturn(new Session());

        ModelAndView mav = authController.registration(authForm, bindingResult, response);

        verify(userService).save(any(User.class));
        assertEquals("redirect:/weather", mav.getViewName());
    }

    @Test
    void test_registration_ExistingLogin_ShouldReturnError() {
        doAnswer(inv -> {
            bindingResult.addError(new ObjectError("authForm", "loginAlreadyExist"));
            return null;
        }).when(userValidator).validateLogin(any(User.class), eq(bindingResult));

        ModelAndView mav = authController.registration(authForm, bindingResult, response);

        assertEquals("auth/sign-up", mav.getViewName());
        assertEquals("loginAlreadyExist", bindingResult.getAllErrors().getFirst().getDefaultMessage());
    }

    @Test
    void test_initSession_ShouldSetCookieWithCorrectAttributes() {
        User user = new User("test", "password");
        Session session = new Session();
        session.setOwner(user);
        session.setSessionId("sessionId");

        when(sessionService.createSession(user)).thenReturn(session);

        ReflectionTestUtils.invokeMethod(authController, "initSession", response, user);

        Cookie cookie = response.getCookie(cookieName);
        assertNotNull(cookie);
        assertEquals("sessionId", cookie.getValue());
        assertTrue(cookie.getSecure());
        assertEquals(1800, cookie.getMaxAge());
    }

    @Test
    void test_logout_ShouldRedirectToLogin() {
        ModelAndView mav = authController.logout("sessionId", response);

        Cookie cookie = response.getCookie(cookieName);
        assertNull(cookie.getValue());
        assertEquals(0, cookie.getMaxAge());

        assertEquals("redirect:/auth/login", mav.getViewName());
    }

}
