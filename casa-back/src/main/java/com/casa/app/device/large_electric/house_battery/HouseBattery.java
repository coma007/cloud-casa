package com.casa.app.device.large_electric.house_battery;

import com.casa.app.device.Device;
import com.casa.app.device.large_electric.house_battery.dto.HouseBatteryDetailsDTO;
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
public class HouseBattery extends Device {
    private double size;

    @Override public HouseBatteryDetailsDTO toDetailsDTO() {
        HouseBatteryDetailsDTO detailsDTO = new HouseBatteryDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());
        detailsDTO.setRealEstateName(this.getRealEstate().getName());
        detailsDTO.setSize(this.size);
        return detailsDTO;
    }
}
