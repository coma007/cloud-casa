package com.casa.app.device.measurement;

import com.casa.app.device.home.air_conditioning.AirConditioningMeasurement;
import com.casa.app.device.home.ambient_sensor.AmbientSensorMeasurement;
import com.casa.app.device.home.washing_machine.WashingMachineMeasurement;
import com.casa.app.device.large_electric.electric_vehicle_charger.ElectricVehicleChargerMeasurement;
import com.casa.app.device.large_electric.house_battery.HouseBatteryMeasurement;
import com.casa.app.device.large_electric.solar_panel_system.SolarPanelSystemMeasurement;
import com.casa.app.device.outdoor.lamp.LampBrightnessMeasurement;
import com.casa.app.device.outdoor.lamp.LampCommandMeasurement;
import com.casa.app.device.outdoor.sprinkler_system.SprinklerSystemMeasurement;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateCommandMeasurement;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateLicencePlatesMeasurement;
import com.casa.app.device.outdoor.vehicle_gate.VehicleGateModeMeasurement;
import com.influxdb.query.FluxRecord;
import org.springframework.stereotype.Service;

@Service
public class MeasurementService {

    public AbstractMeasurement createMeasurement(FluxRecord record) {
        switch (record.getMeasurement()) {
            case (MeasurementType.airConditioning):
                return new AirConditioningMeasurement(
                        // add values from record
                );
            case (MeasurementType.ambientSensor):
                return new AmbientSensorMeasurement(
                        // add values from record
                );
            case (MeasurementType.washingMachine):
                return new WashingMachineMeasurement(
                        // add values from record
                );
            case (MeasurementType.electricVehicleCharger):
                return new ElectricVehicleChargerMeasurement(
                        // add values from record
                );
            case (MeasurementType.houseBattery):
                return new HouseBatteryMeasurement(
                        // add values from record
                );
            case (MeasurementType.solarPanelSystem):
                return new SolarPanelSystemMeasurement(
                        // add values from record
                );
            case (MeasurementType.lampBrightness):
                return new LampBrightnessMeasurement(
                        (String) record.getValueByKey("id"),
                        (Double) record.getValueByKey("brightness"),
                        record.getTime()
                );
            case (MeasurementType.lampCommand):
                return new LampCommandMeasurement(
                        (String) record.getValueByKey("id"),
                        (Boolean) record.getValueByKey("is_on"),
                        (String) record.getValueByKey("user"),
                        record.getTime()
                );
            case (MeasurementType.sprinklerSystem):
                return new SprinklerSystemMeasurement(
                        // add values from record
                );
            case (MeasurementType.vehicleGateLicencePlates):
                return new VehicleGateLicencePlatesMeasurement(
                        (String) record.getValueByKey("id"),
                        (String) record.getValueByKey("licence_plates"),
                        record.getTime()
                );
            case (MeasurementType.vehicleGateCommand):
                return new VehicleGateCommandMeasurement(
                        (String) record.getValueByKey("id"),
                        (Boolean) record.getValueByKey("is_open"),
                        record.getTime()
                );
            case (MeasurementType.vehicleGateMode):
                return new VehicleGateModeMeasurement(
                        (String) record.getValueByKey("id"),
                        (Boolean) record.getValueByKey("is_private"),
                        (String) record.getValueByKey("user"),
                        record.getTime()
                );
            default:
                throw new RuntimeException("Measurement not valid!");
        }
    }
}
