package com.casa.app.request;

import com.casa.app.estate.RealEstateCreateDTO;
import com.casa.app.estate.RealEstateDTO;
import com.casa.app.estate.RealEstateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/realEstateRequest")
public class RealEstateRequestController {

    @Autowired
    RealEstateRequestService realEstateRequestService;

    @PatchMapping("/manage")
    public ResponseEntity<?> manageRequest(@RequestBody RealEstateRequestDTO request) {
        realEstateRequestService.manageRequest(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
