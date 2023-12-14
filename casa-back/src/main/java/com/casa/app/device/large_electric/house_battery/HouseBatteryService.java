package com.casa.app.device.large_electric.house_battery;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.mqtt.MqttGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class HouseBatteryService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private MqttGateway mqttGateway;

    public void handleMessage(Long id, String message) {
        System.out.println(message);
        // TODO: save in influx
    }

    public void manageEnergy(Device device, double power, boolean increase) {
        List<Device> householdDevices = deviceRepository.findAllByRealEstate(device.getRealEstate());
        List<HouseBattery> batteries = new ArrayList<>();
        for (Device d : householdDevices) {
            if (d instanceof HouseBattery) {
                batteries.add((HouseBattery) d);
            }
        }
        double powerPerBattery = power / batteries.size();
        for (HouseBattery b : batteries) {
            if (increase) {
                increaseEnergy(powerPerBattery, b);
            }
            else {
                reduceEnergy(powerPerBattery, b);
            }
        }
    }

    private void increaseEnergy(double powerPerBattery, HouseBattery b) {
        mqttGateway.sendToMqtt(b.getId() + "-INCREASE-" + powerPerBattery, b.getId().toString());
    }

    private void reduceEnergy(double powerPerBattery, HouseBattery b) {
        mqttGateway.sendToMqtt(b.getId() + "-REDUCE-" + powerPerBattery, b.getId().toString());
    }

}
