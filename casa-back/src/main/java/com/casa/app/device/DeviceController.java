package com.casa.app.device;

import com.casa.app.device.dto.DeviceDetailsDTO;
import com.casa.app.device.dto.DeviceRegistrationDTO;
import com.casa.app.device.dto.DeviceSimulationDTO;
import com.casa.app.device.measurement.MeasurementList;
import com.casa.app.device.measurement.OnlineMeasurementList;
import com.casa.app.estate.RealEstateDTO;
import com.casa.app.exceptions.UnathorizedException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.websocket.SocketMessage;
import com.casa.app.websocket.WebSocketController;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/device")
public class DeviceController {

    @Autowired
    private DeviceRegistrationService registrationService;

    @Autowired
    private DeviceService service;

    @PermitAll
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody DeviceRegistrationDTO newDevice) throws UserNotFoundException {
        registrationService.registerDevice(newDevice);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/public/simulation/getAll")
    public ResponseEntity<List<DeviceSimulationDTO>> getAllDevicesForSimulation() {
        List<DeviceSimulationDTO> devices = service.getAllSimulation();
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<List<DeviceSimulationDTO>> getAll() throws UserNotFoundException {
        List<DeviceSimulationDTO> devices = service.getAll();
        return new ResponseEntity<>(devices, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/filter")
    public ResponseEntity<MeasurementList> queryMeasurements(@RequestParam Long id,
                                                             @RequestParam String measurement,
                                                             @RequestParam String from,
                                                             @RequestParam String to,
                                                             @RequestParam String username,
                                                             @RequestParam int page) {
        MeasurementList measurements = service.queryMeasurements(id, measurement, from, to, username, page);
        return new ResponseEntity<>(measurements, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/filterPages")
    public ResponseEntity<Integer> queryPages(@RequestParam Long id,
                                              @RequestParam String measurement,
                                              @RequestParam String from,
                                              @RequestParam String to,
                                              @RequestParam String username) {
        int numOfPages = service.queryNumOfPages(id, measurement, from, to, username);
        return new ResponseEntity<>(numOfPages, HttpStatus.OK);
    }

    @PermitAll
    @GetMapping("/filterActivity")
    public ResponseEntity<OnlineMeasurementList> queryActivity(@RequestParam Long id,
                                              @RequestParam String from,
                                              @RequestParam String to) {
        OnlineMeasurementList activity = service.queryActivity(id, from, to);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @GetMapping("/getAllByOwner")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<List<DeviceDetailsDTO>> getAllByOwner() throws UserNotFoundException {
        return new ResponseEntity<>(service.getAllByOwner(), HttpStatus.OK);
    }

    @GetMapping("/getAllByRealEstate/{id}")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<List<DeviceDetailsDTO>> getAllByRealEstate(@PathVariable Long id) throws UserNotFoundException {
        return new ResponseEntity<>(service.getAllByRealEstate(id), HttpStatus.OK);
    }

    @GetMapping("/getDeviceDetails/{id}")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<DeviceDetailsDTO> getDeviceDetails(@PathVariable Long id) throws UserNotFoundException, UnathorizedException {
        return new ResponseEntity<>(service.getDeviceDetails(id), HttpStatus.OK);
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
