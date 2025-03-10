package com.devminrat.weatherApp.utils;

import com.devminrat.weatherApp.models.User;
import com.devminrat.weatherApp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;


@Component
public class UserValidator implements Validator {
    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        if (user.getLogin() == null || user.getLogin().trim().isEmpty()) {
            errors.rejectValue("login", "login.empty", "Login is required");
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            errors.rejectValue("password", "password.empty", "Password is required");
        }
    }

    public void validateLogin(User user, Errors errors) {
        Optional<User> existingUser = userService.findUserByLogin(user.getLogin());
        if (existingUser.isPresent()) {
            errors.rejectValue("login", "login.exist", "Login already exists");
        }
    }

    public void validatePassword(User user, String password, Errors errors) {
        if (!user.getPassword().equals(password)) {
            errors.rejectValue("password", "password.invalid", "Password does not match");
        }
    }
}
