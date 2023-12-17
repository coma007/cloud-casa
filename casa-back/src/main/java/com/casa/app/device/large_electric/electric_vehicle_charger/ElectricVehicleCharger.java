package com.casa.app.device.large_electric.electric_vehicle_charger;

import com.casa.app.device.Device;
import com.casa.app.device.large_electric.electric_vehicle_charger.dto.ElectricVehicleChargerDetailsDTO;
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
public class ElectricVehicleCharger extends Device {
    private int chargePower;
    private int numOfSlots;

    @Override public ElectricVehicleChargerDetailsDTO toDetailsDTO() {
        ElectricVehicleChargerDetailsDTO detailsDTO = new ElectricVehicleChargerDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());
        detailsDTO.setRealEstateName(this.getRealEstate().getName());
        detailsDTO.setChargePower(this.chargePower);
        detailsDTO.setNumOfSlots(this.numOfSlots);
        return detailsDTO;
    }
}
