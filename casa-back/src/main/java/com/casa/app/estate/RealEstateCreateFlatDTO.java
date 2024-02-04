package com.casa.app.estate;

import com.casa.app.location.Address;
import com.casa.app.location.CityDTO;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.UpperCamelCaseStrategy.class)
public class RealEstateCreateFlatDTO {

    private String name;
    private String address;
    private double longitude;
    private double latitude;
    private String type;
    private double size;
    private int numberOfFloors;
    private String cityName;
    private String cityCountry;
    private MultipartFile file;

    public RealEstateCreateDTO unflat(){
        RealEstateCreateDTO dto = new RealEstateCreateDTO();
        dto.setName(name);
        dto.setAddress(new Address(address, longitude, latitude));
        dto.setType(type);
        dto.setSize(size);
        dto.setNumberOfFloors(numberOfFloors);
        dto.setCity(new CityDTO(cityName, cityCountry));
        return dto;
    }
}
