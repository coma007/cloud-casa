package com.casa.app.location;

import com.casa.app.estate.RealEstateDTO;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/location")
public class LocationController {

    @Autowired
    LocationService locationService;

    @GetMapping("/getAllCountries")
    public ResponseEntity<List<String>> getAllCountries() {
        return new ResponseEntity<>(locationService.getAllCountries(), HttpStatus.OK);
    }

    @GetMapping("/getAllCities/{country}")
    public ResponseEntity<List<CityDTO>> getAllCities(@PathVariable String country) {
        return new ResponseEntity<>(locationService.getAllCities(country), HttpStatus.OK);
    }
}
