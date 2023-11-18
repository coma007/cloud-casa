package com.casa.app.location;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityDTO {
    private String name;
    private String country;

    public CityDTO(City city) {
        this.name = city.getName();
        this.country = city.getCountry().getName();
    }
}
