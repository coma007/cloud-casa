package com.casa.app.request;

import com.casa.app.estate.RealEstateDTO;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/realEstateRequest")
public class RealEstateRequestController {

    @Autowired
    RealEstateRequestService realEstateRequestService;

    @PatchMapping("/manage")
    public ResponseEntity<?> manageRequest(@RequestBody RealEstateRequestDTO request) throws MessagingException, IOException {
        realEstateRequestService.manage(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<RealEstateDTO>> getAll() throws MessagingException, UnsupportedEncodingException {
        List<RealEstateDTO> requests = realEstateRequestService.getAllRealEstate();

        return new ResponseEntity<>(requests, HttpStatus.OK);
    }
    @GetMapping("/getAllUnresolved")
    public ResponseEntity<List<RealEstateDTO>> getAllUnresolved() throws MessagingException, UnsupportedEncodingException {
        List<RealEstateDTO> requests = realEstateRequestService.getAllUnresolvedRealEstate();
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }
}
