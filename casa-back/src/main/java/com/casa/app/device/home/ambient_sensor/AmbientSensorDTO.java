package com.casa.app.device.home.ambient_sensor;

import com.casa.app.device.dto.DeviceDTO;
import com.casa.app.device.DeviceStatus;
import com.casa.app.device.PowerSupplyType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
//@NoArgsConstructor
@AllArgsConstructor
public class AmbientSensorDTO extends DeviceDTO {

    public AmbientSensor toModel() {
        AmbientSensor device = new AmbientSensor();
        device.setName(this.getName());
        device.setStatus(DeviceStatus.OFFLINE);
        device.setEnergyConsumption(this.getEnergyConsumption());
        device.setPowerSupplyType(PowerSupplyType.valueOf(this.getPowerSupplyType()));
//        device.setCredentials();
//        device.setRealEstate();
        return device;
    }
}
