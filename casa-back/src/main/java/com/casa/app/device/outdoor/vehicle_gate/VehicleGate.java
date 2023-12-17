package com.casa.app.device.outdoor.vehicle_gate;

import com.casa.app.device.Device;
import com.casa.app.device.outdoor.vehicle_gate.dto.VehicleGateDetailsDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class VehicleGate extends Device {
    private VehicleGateMode currentMode;
    private List<String> allowedVehicles;

    @Override public VehicleGateDetailsDTO toDetailsDTO() {
        VehicleGateDetailsDTO detailsDTO = new VehicleGateDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());
        detailsDTO.setRealEstateName(this.getRealEstate().getName());
        detailsDTO.setCurrentMode(this.currentMode.toString());
        detailsDTO.setAllowedVehicles(new ArrayList<>());
        for (String v : this.allowedVehicles) {
            detailsDTO.getAllowedVehicles().add(v);
        }
        return detailsDTO;
    }
}
