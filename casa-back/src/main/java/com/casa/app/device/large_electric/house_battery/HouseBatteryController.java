package com.casa.app.device.large_electric.house_battery;

import com.casa.app.device.large_electric.house_battery.dto.CityPowerUsageDTO;
import com.casa.app.device.large_electric.house_battery.dto.HouseBatterySimulationDTO;
import com.casa.app.device.large_electric.house_battery.dto.RealEstatePowerUsageDTO;
import com.casa.app.exceptions.NotFoundException;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/houseBattery")
public class HouseBatteryController {

    @Autowired
    private HouseBatteryService houseBatteryService;

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<HouseBatterySimulationDTO>> getAll() {
        return new ResponseEntity<>(houseBatteryService.getAllSimulation(), HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/powerUsage")
    public ResponseEntity<List<RealEstatePowerUsageDTO>> getPowerUsage(@RequestParam String from,
                                           @RequestParam String to) {
        List<RealEstatePowerUsageDTO> estates = houseBatteryService.powerUsageByEstate(from, to);
        return new ResponseEntity<>(estates, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/powerUsageForCity")
    public ResponseEntity<List<CityPowerUsageDTO>> getCityPowerUsage(@RequestParam String from,
                                           @RequestParam String to) {
        List<CityPowerUsageDTO> cities = houseBatteryService.powerUsageByCity(from, to);
        return new ResponseEntity<>(cities, HttpStatus.OK);
    }
}
