package com.casa.app.device.large_electric.house_battery;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.large_electric.house_battery.dto.HouseBatterySimulationDTO;
import com.casa.app.device.large_electric.house_battery.measurement.HouseBatteryCurrentStateMeasurement;
import com.casa.app.device.large_electric.house_battery.measurement.HouseBatteryImportExportMeasurement;
import com.casa.app.device.large_electric.house_battery.measurement.HouseBatteryPowerUsageMeasurement;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.mqtt.MqttGateway;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class HouseBatteryService {

    @Autowired
    private DeviceRepository deviceRepository;
    @Autowired
    private HouseBatteryRepository houseBatteryRepository;
    @Autowired
    private MqttGateway mqttGateway;
    @Autowired
    private InfluxDBService influxDBService;
    @Autowired
    private WebSocketController webSocketController;

    public void handleExportImport(Long id, String message) {
        System.out.println(message);
        HouseBattery battery = houseBatteryRepository.findById(id).orElse(null);
        if (battery == null) {
            return;
        }
        double value;
        String type;
        try {
            value = Double.parseDouble(message);
        } catch (NumberFormatException e) {
            return;
        }
        if (value < 0) {
            type = "Import";
        } else {
            type = "Export";
        }
        influxDBService.write(new HouseBatteryImportExportMeasurement(battery.getId(), type, value, Instant.now()));
    }

    public void handlePowerUsage(Long id, String message) {
        HouseBattery battery = houseBatteryRepository.findById(id).orElse(null);
        if (battery == null) {
            return;
        }
        double value;
        try {
            value = Double.parseDouble(message);
        } catch (NumberFormatException e) {
            return;
        }
        HouseBatteryPowerUsageMeasurement measurement = new HouseBatteryPowerUsageMeasurement(battery.getId(), value, Instant.now());
        influxDBService.write(measurement);
//        TODO to epoch second
        measurement.setTimestamp(measurement.getTimestamp());
        webSocketController.sendMessage(new SocketMessage<HouseBatteryPowerUsageMeasurement>("house-battery-power-usage", "New value", null, id.toString(), measurement));
    }

    public void handleBatteryState(Long id, String message) {
        HouseBattery battery = houseBatteryRepository.findById(id).orElse(null);
        if (battery == null) {
            return;
        }
        double value;
        try {
            value = Double.parseDouble(message);
        } catch (NumberFormatException e) {
            return;
        }
        influxDBService.write(new HouseBatteryCurrentStateMeasurement(battery.getId(), value, Instant.now()));
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
                double reducedPower = powerPerBattery / 60;
                reduceEnergy(reducedPower, b);
            }
        }
    }

    private void increaseEnergy(double powerPerBattery, HouseBattery b) {
        mqttGateway.sendToMqtt(b.getId() + "~INCREASE~" + powerPerBattery, b.getId().toString());
    }

    private void reduceEnergy(double powerPerBattery, HouseBattery b) {
        mqttGateway.sendToMqtt(b.getId() + "~REDUCE~" + powerPerBattery, b.getId().toString());
    }

    public List<HouseBatterySimulationDTO> getAllSimulation() {
        List<HouseBattery> HouseBatteries = houseBatteryRepository.findAll();
        List<HouseBatterySimulationDTO> HouseBatteryDTOS = new ArrayList<>();
        for (HouseBattery b : HouseBatteries) {
            HouseBatteryDTOS.add(new HouseBatterySimulationDTO(b.getId(), b.getSize(), b.getSize()*0.9));
        }
        return HouseBatteryDTOS;
    }

}
