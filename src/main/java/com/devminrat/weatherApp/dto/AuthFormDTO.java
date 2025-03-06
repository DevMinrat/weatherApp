package com.devminrat.weatherApp.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class AuthFormDTO {
    @NotEmpty(message = "Login should not be empty")
    @Size(min = 2, max = 30, message = "Login length should be between 2 and 30")
    private String login;

    @NotEmpty(message = "Password should not be empty")
    @Size(min = 4, max = 30, message = "Password length should be between 4 and 30")
    private String password;

}
