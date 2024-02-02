package com.casa.app.device.large_electric.house_battery;

import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.DeviceService;
import com.casa.app.device.large_electric.house_battery.dto.CityPowerUsageDTO;
import com.casa.app.device.large_electric.house_battery.dto.HouseBatterySimulationDTO;
import com.casa.app.device.large_electric.house_battery.dto.RealEstatePowerUsageDTO;
import com.casa.app.device.large_electric.house_battery.measurement.HouseBatteryCurrentStateMeasurement;
import com.casa.app.device.large_electric.house_battery.measurement.HouseBatteryImportExportMeasurement;
import com.casa.app.device.large_electric.house_battery.measurement.HouseBatteryPowerUsageMeasurement;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystem;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystemRepository;
import com.casa.app.device.large_electric.solar_panel_system.measurement.SolarPanelSystemPowerMeasurement;
import com.casa.app.device.measurement.AbstractMeasurement;
import com.casa.app.device.measurement.MeasurementList;
import com.casa.app.device.measurement.MeasurementType;
import com.casa.app.estate.RealEstate;
import com.casa.app.estate.RealEstateRepository;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.influxdb.InfluxDBService;
import com.casa.app.location.City;
import com.casa.app.location.CityRepository;
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
    private CityRepository cityRepository;
    @Autowired
    private HouseBatteryRepository houseBatteryRepository;
    @Autowired
    private SolarPanelSystemRepository solarPanelSystemRepository;
    @Autowired
    private DeviceService deviceService;
    @Autowired
    private RealEstateRepository realEstateRepository;
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

    public List<RealEstatePowerUsageDTO> powerUsageByEstate(String from, String to) {
        List<RealEstate> estates = realEstateRepository.findAll();
        List<RealEstatePowerUsageDTO> estatesPowerUsage = new ArrayList<>();
        for (RealEstate e : estates) {
            double power = 0;
            double production = 0;
//            List<HouseBattery> batteries = houseBatteryRepository.findAllByRealEstate(e);
//            for (HouseBattery b : batteries) {
//                MeasurementList measurements = deviceService.fullQueryMeasurements(b.getId(), MeasurementType.houseBatteryPowerUsage, from, to, "");
//                for (AbstractMeasurement m : measurements.getMeasurements()) {
//                    power += ((HouseBatteryPowerUsageMeasurement)m).getPower();
//                }
//            }

            List<HouseBattery> batteries = houseBatteryRepository.findAllByRealEstate(e);
            power = batteries.stream()
                    .flatMap(b -> deviceService.fullQueryMeasurements(b.getId(), MeasurementType.houseBatteryPowerUsage, from, to, "").getMeasurements().stream())
                    .filter(m -> m instanceof HouseBatteryPowerUsageMeasurement)
                    .mapToDouble(m -> ((HouseBatteryPowerUsageMeasurement)m).getPower())
                    .sum();
            
//            List<SolarPanelSystem> systems = solarPanelSystemRepository.findAllByRealEstate(e);
//            for (SolarPanelSystem s : systems) {
//                MeasurementList measurements = deviceService.fullQueryMeasurements(s.getId(), MeasurementType.solarPanelSystem, from, to, "");
//                for (AbstractMeasurement m : measurements.getMeasurements()) {
//                    production += ((SolarPanelSystemPowerMeasurement)m).getPower();
//                }
//            }
            List<SolarPanelSystem> systems = solarPanelSystemRepository.findAllByRealEstate(e);

            production = systems.stream()
                    .flatMap(s -> deviceService.fullQueryMeasurements(s.getId(), MeasurementType.solarPanelSystem, from, to, "").getMeasurements().stream())
                    .filter(m -> m instanceof SolarPanelSystemPowerMeasurement)
                    .mapToDouble(m -> ((SolarPanelSystemPowerMeasurement) m).getPower())
                    .sum();

            estatesPowerUsage.add(new RealEstatePowerUsageDTO(e.getName(), power, production));
        }
        return estatesPowerUsage;
    }

    public List<CityPowerUsageDTO> powerUsageByCity(String from, String to){
        List<City> cities = cityRepository.findAll();
        List<CityPowerUsageDTO> cityPowerUsageDTOS = new ArrayList<>();
        for (City c : cities) {
            List<RealEstate> estates = realEstateRepository.findAllByCity(c);
            double power = 0;
            double production = 0;
            for (RealEstate e : estates) {
                List<HouseBattery> batteries = houseBatteryRepository.findAllByRealEstate(e);
                for (HouseBattery b : batteries) {
                    int pages = deviceService.queryNumOfPages(b.getId(), MeasurementType.houseBatteryPowerUsage, from, to, "");
                    for (int i = 1; i <= pages; i++) {
                        MeasurementList measurements = deviceService.queryMeasurements(b.getId(), MeasurementType.houseBatteryPowerUsage, from, to, "", i);
                        for (AbstractMeasurement m : measurements.getMeasurements()) {
                            power += ((HouseBatteryPowerUsageMeasurement) m).getPower();
                        }
                    }
                }
                List<SolarPanelSystem> systems = solarPanelSystemRepository.findAllByRealEstate(e);
                for (SolarPanelSystem s : systems) {
                    int pages = deviceService.queryNumOfPages(s.getId(), MeasurementType.solarPanelSystem, from, to, "");
                    for (int i = 1; i <= pages; i++) {
                        MeasurementList measurements = deviceService.queryMeasurements(s.getId(), MeasurementType.solarPanelSystem, from, to, "", i);
                        for (AbstractMeasurement m : measurements.getMeasurements()) {
                            production += ((SolarPanelSystemPowerMeasurement) m).getPower();
                        }
                    }
                }
            }
            cityPowerUsageDTOS.add(new CityPowerUsageDTO(c.getName(), power, production));
        }
        return cityPowerUsageDTOS;
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
