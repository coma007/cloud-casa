package com.casa.app.estate;

import com.casa.app.location.City;
import com.casa.app.location.CityDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateCreateDTO {

    private String name;
    private String address;
    private String type;
    private double size;
    private int numberOfFloors;
    private CityDTO city;
}
