package com.casa.app.device.large_electric.solar_panel_system;

import com.casa.app.device.DeviceStatus;
import com.casa.app.device.large_electric.solar_panel_system.dto.SolarPanelSystemSimulationDTO;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/solarPanelSystem")
public class SolarPanelSystemController {

    @Autowired
    private SolarPanelSystemService solarPanelSystemService;

    @Autowired
    private WebSocketController webSocketController;

    @PermitAll
    @PostMapping("/toggleStatus/{id}")
    public ResponseEntity<?> toggleStatus(@PathVariable Long id) throws UserNotFoundException {
        DeviceStatus newStatus;
        if ((newStatus = solarPanelSystemService.toggleStatus(id)) != null) {
            webSocketController.sendMessage(new SocketMessage<>("solar-panel-system-status", newStatus.toString(), null, id.toString(), null));
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<SolarPanelSystemSimulationDTO>> getAll() {
        return new ResponseEntity<>(solarPanelSystemService.getAllSimulation(), HttpStatus.OK);
    }
}
