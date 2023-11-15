package com.casa.app.estates;


import com.casa.app.estates.request.RealEstateRequest;
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

}
