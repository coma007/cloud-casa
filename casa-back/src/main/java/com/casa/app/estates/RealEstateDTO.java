package com.casa.app.estates;

import com.casa.app.estates.request.RealEstateRequest;
import com.casa.app.location.City;
import com.casa.app.permission.real_estate_permission.RealEstatePermission;
import jakarta.persistence.*;

import java.security.Permission;

public class RealEstateDTO {

    private String name;
    private String address;
    private String type;
    private double size;
    private int numberOfFloors;
    private City city;
    private RealEstateRequest request;
    private RealEstatePermission owner;


    public RealEstateDTO(RealEstate estate, RealEstatePermission permission) {
        this.name = estate.getName();
        this.address = estate.getAddress();
        this.type = estate.getType().name();
        this.size = estate.getSize();
        this.numberOfFloors = estate.getNumberOfFloors();
        this.city = estate.getCity();
        this.request = estate.getRequest();
        this.owner = permission;
    }
}
