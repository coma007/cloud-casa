package com.casa.app.device.large_electric.solar_panel_system;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/solar-panel-system")
public class SolarPanelSystemController {

    @Autowired
    private SolarPanelSystemService solarPanelSystemService;

    @PermitAll
    @PostMapping("/toggleStatus/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id) {
        if (solarPanelSystemService.toggleStatus(id)) {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
