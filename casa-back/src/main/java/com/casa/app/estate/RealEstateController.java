package com.casa.app.estate;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UnathorizedReadException;
import com.casa.app.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/realEstate")
public class RealEstateController {

    @Autowired
    RealEstateService realEstateService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<RealEstateDTO> create(@RequestBody RealEstateCreateDTO estateDTO) throws UserNotFoundException {
        return new ResponseEntity<>(realEstateService.create(estateDTO), HttpStatus.OK);
    }


    @GetMapping("/getAllByOwner")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<List<RealEstateDTO>> getAllByOwner() throws UserNotFoundException {
        return new ResponseEntity<>(realEstateService.getAllByOwner(), HttpStatus.OK);
    }


    @GetMapping("/getAll")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<List<RealEstateDTO>> getAll() throws UserNotFoundException {
        return new ResponseEntity<>(realEstateService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/getAllApprovedByOwner")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<List<RealEstateDTO>> getAllApprovedByOwner() throws UserNotFoundException {
        return new ResponseEntity<>(realEstateService.getAllApprovedByOwner(), HttpStatus.OK);
    }

    @GetMapping("/getAllApproved")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<List<RealEstateDTO>> getAllApproved() throws UserNotFoundException {
        return new ResponseEntity<>(realEstateService.getAllApproved(), HttpStatus.OK);
    }


    @GetMapping("/isOwner/{id}")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<?> isOwner(@PathVariable Long id) throws UserNotFoundException, NotFoundException {
        return new ResponseEntity<>(realEstateService.isOwner(id), HttpStatus.OK);
    }
}
