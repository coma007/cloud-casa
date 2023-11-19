package com.casa.app.device;

import com.casa.app.estate.RealEstate;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;

    private String name;
    private PowerSupplyType powerSupplyType;
    private double energyConsumption;
    private DeviceStatus status;
    @ManyToOne
    private RealEstate realEstate;

    @Cascade(CascadeType.ALL)
    @OneToOne
    private ConnectionCredentials credentials;
}
