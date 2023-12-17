package com.casa.app.device.outdoor.sprinkler_system;

import com.casa.app.device.Device;
import com.casa.app.device.outdoor.sprinkler_system.dto.SprinklerSystemDetailsDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
//@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class SprinklerSystem extends Device {

    @Override public SprinklerSystemDetailsDTO toDetailsDTO() {
        SprinklerSystemDetailsDTO detailsDTO = new SprinklerSystemDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());
        detailsDTO.setRealEstateName(this.getRealEstate().getName());
        return detailsDTO;
    }
}
