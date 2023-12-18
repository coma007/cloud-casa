package com.casa.app.device.home.ambient_sensor;

import com.casa.app.device.Device;
import com.casa.app.device.home.ambient_sensor.dto.AmbientSensorDetailsDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@AllArgsConstructor
//@NoArgsConstructor
public class AmbientSensor extends Device {

    @Override public AmbientSensorDetailsDTO toDetailsDTO() {
        AmbientSensorDetailsDTO detailsDTO = new AmbientSensorDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());
        detailsDTO.setRealEstateName(this.getRealEstate().getName());
        return detailsDTO;
    }
}
