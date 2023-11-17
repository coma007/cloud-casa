package com.casa.app.estate;


import com.casa.app.request.RealEstateRequest;
import com.casa.app.location.City;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RealEstate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private RealEstateType type;
    private double size;
    private int numberOfFloors;
    private String imageExtension;

    @ManyToOne
    private City city;

    @OneToOne
    private RealEstateRequest request;

    public RealEstate(RealEstateCreateDTO estate) {
        this.name = estate.getName();
        this.address = estate.getAddress();
        this.type = RealEstateType.valueOf(estate.getType().toUpperCase());
        this.size = estate.getSize();
        this.numberOfFloors = estate.getNumberOfFloors();
        this.imageExtension = ""; // TODO setup image extension
    }
}
