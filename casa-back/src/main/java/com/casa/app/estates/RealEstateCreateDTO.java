package com.casa.app.estates;

import com.casa.app.estates.request.RealEstateRequest;
import com.casa.app.location.City;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Permission;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealEstateCreateDTO {

    private String name;
    private String address;
    private String type;
    private double size;
    private int numberOfFloors;
    private City city;
}
