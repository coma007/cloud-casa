package com.casa.app.estate;


import com.casa.app.location.Address;
import com.casa.app.permission.real_estate_permission.RealEstatePermission;
import com.casa.app.request.RealEstateRequest;
import com.casa.app.location.City;
import com.casa.app.util.email.FileUtil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealEstate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Embedded
    private Address address;
    private RealEstateType type;
    private double size;
    private int numberOfFloors;
    private String imageExtension;
    @ManyToOne
    private City city;
    @OneToOne
    private RealEstateRequest request;
    @OneToOne
    private RealEstatePermission owner;

    public RealEstate(RealEstateCreateDTO estate, City city) {
        this.name = estate.getName();
        this.address = estate.getAddress();
        this.type = RealEstateType.valueOf(estate.getType().toUpperCase());
        this.size = estate.getSize();
        this.numberOfFloors = estate.getNumberOfFloors();
        this.imageExtension = ""; // TODO setup image extension
        this.city = city;
    }
}
