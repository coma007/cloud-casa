package com.casa.app.device;

import com.casa.app.estate.RealEstate;
import com.casa.app.user.regular_user.RegularUser;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.util.Date;

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
    private RegularUser owner;
    private PowerSupplyType powerSupplyType;
    private double energyConsumption;
    private DeviceStatus status;
    private Date lastSeen;
    @ManyToOne
    private RealEstate realEstate;

    @Cascade(CascadeType.ALL)
    @OneToOne
    private ConnectionCredentials credentials;
}
