package com.casa.app.device.home.air_conditioning;

import com.casa.app.device.Device;
import com.casa.app.device.dto.DeviceDetailsDTO;
import com.casa.app.device.home.air_conditioning.dto.AirConditioningDetailsDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

    @Override public AirConditioningDetailsDTO toDetailsDTO() {
        AirConditioningDetailsDTO detailsDTO = new AirConditioningDetailsDTO();
        detailsDTO.setStatus(this.getStatus().toString());
        detailsDTO.setId(this.getId());
        detailsDTO.setName(this.getName());
        detailsDTO.setEnergyConsumption(this.getEnergyConsumption());
        detailsDTO.setPowerSupplyType(this.getPowerSupplyType().toString());
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
