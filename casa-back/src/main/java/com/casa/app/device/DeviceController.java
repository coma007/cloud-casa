package com.casa.app.device;

import com.casa.app.device.dto.DeviceRegistrationDTO;
import com.casa.app.device.dto.DeviceSimulationDTO;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private DeviceRegistrationService registrationService;

    @Autowired
    private DeviceService service;

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DeviceRegistrationDTO newDevice){
        registrationService.registerDevice(newDevice);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<DeviceSimulationDTO>> getAllDevicesForSimulation() {
        List<DeviceSimulationDTO> devices = service.getAllSimulation();
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }


    @Autowired
    private WebSocketController webSocketController;

    @PermitAll
    @GetMapping("/public/websocket")
    public ResponseEntity<?> sendMessage() {
        webSocketController.sendMessage(new SocketMessage<>("myTopic", "MOJA PORUKA", null, null, null));
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
