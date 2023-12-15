package com.casa.app.device.home.ambient_sensor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmbientSensorService {

    @Autowired
    private AmbientSensorRepository ambientSensorRepository;

    public List<AmbientSensorSimulationDTO> getAllSimulation() {
        List<AmbientSensor> ambientSensors = ambientSensorRepository.findAll();
        List<AmbientSensorSimulationDTO> ambientSensorDTOS = new ArrayList<>();
        for (AmbientSensor a : ambientSensors) {
            ambientSensorDTOS.add(new AmbientSensorSimulationDTO(a.getId()));
        }
        return ambientSensorDTOS;
    }
}
