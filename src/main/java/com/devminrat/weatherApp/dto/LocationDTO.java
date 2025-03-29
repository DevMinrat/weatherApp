package com.devminrat.weatherApp.dto;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class LocationDTO {
    private String name;
    private BigDecimal lat;
    private BigDecimal lon;
}
