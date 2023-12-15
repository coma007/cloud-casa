package com.casa.app.device.home.air_conditioning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirConditioningService {

    @Autowired
    private AirConditioningRepository airConditioningRepository;

    public List<AirConditioningSimulationDTO> getAllSimulation() {
        List<AirConditioning> airConditioners = airConditioningRepository.findAll();
        List<AirConditioningSimulationDTO> airConditionerDTOS = new ArrayList<>();
        for (AirConditioning a : airConditioners) {
            airConditionerDTOS.add(new AirConditioningSimulationDTO(a.getId(), a.getMinTemperature(),
                    a.getMaxTemperature(), a.getSupportedModes()));
        }
        return airConditionerDTOS;
    }
}
