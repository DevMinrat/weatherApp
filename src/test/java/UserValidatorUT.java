import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.services.UserService;
import com.devminrat.weatherApp.utils.UserValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserValidatorUT {
    @Mock
    private UserService userService;

    @InjectMocks
    UserValidator userValidator;

    @Test
    public void test_validateEmptyLogin() {
        User user = new User("", "password");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        userValidator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("login"));
        assertEquals("Login is required", errors.getFieldError("login").getDefaultMessage());
    }

    @Test
    public void test_validateEmptyPassword() {
        User user = new User("login", "");
        Errors errors = new BeanPropertyBindingResult(user, "user");

        userValidator.validate(user, errors);

        assertTrue(errors.hasFieldErrors("password"));
        assertEquals("Password is required", errors.getFieldError("password").getDefaultMessage());
    }

    @Test
    public void test_validateExistedLogin() {
        User user = new User("login", "password");
        Errors errors = new BeanPropertyBindingResult(user, "user");
        when(userService.findUserByLogin(user.getLogin())).thenReturn(Optional.of(user));

        userValidator.validateLogin(user, errors);

        assertTrue(errors.hasFieldErrors("login"));
        assertEquals("Login already exists", errors.getFieldError("login").getDefaultMessage());
    }

    @Test
    public void test_validateInvalidPassword() {
        User user = new User("login", "encodedPassword");
        Errors errors = new BeanPropertyBindingResult(user, "user");
        when(userService.checkPassword("rawPassword", "encodedPassword"))
                .thenReturn(false);

        userValidator.validatePassword(user, "rawPassword", errors);

        assertTrue(errors.hasFieldErrors("password"));
        assertEquals("Password does not match", errors.getFieldError("password").getDefaultMessage());
    }
}
