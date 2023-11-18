package com.casa.app.request;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequestMapping("/api/realEstateRequest")
public class RealEstateRequestController {

    @Autowired
    RealEstateRequestService realEstateRequestService;

    @PatchMapping("/manage")
    public ResponseEntity<?> manageRequest(@RequestBody RealEstateRequestDTO request) throws MessagingException, UnsupportedEncodingException {
        realEstateRequestService.manage(request);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<RealEstateRequestDTO>> getAll(@RequestParam(required = false) Boolean approved) throws MessagingException, UnsupportedEncodingException {
        List<RealEstateRequestDTO> requests;
        if (approved == null) {
            requests = realEstateRequestService.getAll();
        }
        else {
            requests = realEstateRequestService.getAll(approved);
        }
        return new ResponseEntity<>(requests, HttpStatus.OK);
    }
}
