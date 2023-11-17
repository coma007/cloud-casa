package com.casa.app.estate;

import com.casa.app.request.RealEstateRequest;
import com.casa.app.location.City;
import com.casa.app.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstateDTO {

    private String name;
    private String address;
    private String type;
    private double size;
    private int numberOfFloors;
    private City city;
    private RealEstateRequest request;
    private User owner; // TODO change to dto


    public RealEstateDTO(RealEstate estate, User user) {
        this.name = estate.getName();
        this.address = estate.getAddress();
        this.type = estate.getType().name();
        this.size = estate.getSize();
        this.numberOfFloors = estate.getNumberOfFloors();
        this.city = estate.getCity();
        this.request = estate.getRequest();
        this.owner = user;
    }
}
