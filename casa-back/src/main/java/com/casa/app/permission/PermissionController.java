package com.casa.app.permission;


import com.casa.app.estate.RealEstateCreateDTO;
import com.casa.app.estate.RealEstateDTO;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.permission.dto.NewPermissionDTO;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @Autowired
    RealEstatePermissionService realEstatePermissionService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<?> create(@RequestBody NewPermissionDTO dto) throws UserNotFoundException {
        if(!(dto.getKind().equalsIgnoreCase("REAL ESTATE") || dto.getKind().equalsIgnoreCase("DEVICE") ))
            return ResponseEntity.badRequest().body("Invalid kind given");
        return new ResponseEntity<>(permissionService.create(dto), HttpStatus.OK);
    }
}
