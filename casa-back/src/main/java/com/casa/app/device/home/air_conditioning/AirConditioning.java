package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.Device;
import com.casa.app.device.home.air_conditioning.dto.AirConditioningDetailsDTO;
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
public class AirConditioning extends Device {
    private int minTemperature;
    private int maxTemperature;
    private List<AirConditioningMode> supportedModes;


//    private Double currentTargetTemperature;
//    private Boolean working;
//    private String mode;
    @Override public AirConditioningDetailsDTO toDetailsDTO() {
        AirConditioningDetailsDTO detailsDTO = new AirConditioningDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());

        if(getRealEstate() == null)
            detailsDTO.setRealEstateName(null);
        else
            detailsDTO.setRealEstateName(this.getRealEstate().getName());

        detailsDTO.setMinTemperature(this.minTemperature);
        detailsDTO.setMaxTemperature(this.maxTemperature);
        detailsDTO.setSupportedModes(new ArrayList<>());
        for (AirConditioningMode m : this.supportedModes) {
            detailsDTO.getSupportedModes().add(m.toString());
        }
        return detailsDTO;
    }
}
