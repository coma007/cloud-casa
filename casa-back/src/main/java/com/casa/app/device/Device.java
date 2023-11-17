package com.casa.app.device;

import com.casa.app.estate.RealEstate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private PowerSupplyType powerSupplyType;
    private double energyConsumption;

    @ManyToOne
    private RealEstate realEstate;

    @OneToOne
    private ConnectionCredentials credentials;
}
