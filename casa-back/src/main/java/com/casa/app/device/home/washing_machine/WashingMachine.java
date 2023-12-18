package com.casa.app.device.home.washing_machine;

import com.casa.app.device.Device;
import com.casa.app.device.home.washing_machine.dto.WashingMachineDetailsDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
public class WashingMachine extends Device {

    public List<WashingMachineMode> supportedMods;

    @Override public WashingMachineDetailsDTO toDetailsDTO() {
        WashingMachineDetailsDTO detailsDTO = new WashingMachineDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());
        detailsDTO.setRealEstateName(this.getRealEstate().getName());
        detailsDTO.setSupportedModes(new ArrayList<>());
        for (WashingMachineMode m : this.supportedMods) {
            detailsDTO.getSupportedModes().add(m.toString());
        }
        return detailsDTO;
    }
}
