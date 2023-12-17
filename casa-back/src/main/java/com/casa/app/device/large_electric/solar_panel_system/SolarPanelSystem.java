package com.casa.app.device.large_electric.solar_panel_system;

import com.casa.app.device.Device;
import com.casa.app.device.large_electric.solar_panel_system.dto.SolarPanelSystemDetailsDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class SolarPanelSystem extends Device {
    private double size;
    private double efficiency;

    @Override public SolarPanelSystemDetailsDTO toDetailsDTO() {
        SolarPanelSystemDetailsDTO detailsDTO = new SolarPanelSystemDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());
        detailsDTO.setRealEstateName(this.getRealEstate().getName());
        detailsDTO.setSize(this.size);
        detailsDTO.setEfficiency(this.efficiency);
        return detailsDTO;
    }

}
