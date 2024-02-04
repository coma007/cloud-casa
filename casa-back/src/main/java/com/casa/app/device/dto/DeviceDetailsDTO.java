package com.casa.app.device.dto;


import com.casa.app.user.regular_user.dtos.RegularUserDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceDetailsDTO implements Serializable {

    private Long id;
    private String type;
    private String status;
    private String name;
    private String powerSupplyType;
    private double energyConsumption;
    private String realEstateName;

    private RegularUserDTO owner;
}
