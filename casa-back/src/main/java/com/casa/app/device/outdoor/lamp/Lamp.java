package com.casa.app.device.outdoor.lamp;

import com.casa.app.device.Device;
import com.casa.app.device.outdoor.lamp.dto.LampDetailsDTO;
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
public class Lamp extends Device {

    @Override public LampDetailsDTO toDetailsDTO() {
        LampDetailsDTO detailsDTO = new LampDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());
        detailsDTO.setRealEstateName(this.getRealEstate().getName());
        return detailsDTO;
    }
}
