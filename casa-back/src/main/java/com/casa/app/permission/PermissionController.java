package com.casa.app.permission;


import com.casa.app.exceptions.AlreadyExistsException;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UnauthorizedWriteException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.permission.dto.PermissionDTO;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
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
    @Autowired
    private RegularUserService regularUserService;

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<?> create(@RequestBody PermissionDTO dto) throws UserNotFoundException, NotFoundException, UnauthorizedWriteException, AlreadyExistsException {
        if(!(dto.getKind().equalsIgnoreCase("REAL ESTATE") || dto.getKind().equalsIgnoreCase("DEVICE") ))
            return ResponseEntity.badRequest().body("Invalid kind given");
        return new ResponseEntity<>(permissionService.create(dto), HttpStatus.OK);
    }

    @PostMapping("/delete")
    @PreAuthorize("hasAnyAuthority('regular user')")
    public ResponseEntity<?> delete(@RequestBody PermissionDTO dto) throws UserNotFoundException, NotFoundException, UnauthorizedWriteException {
        if(!(dto.getKind().equalsIgnoreCase("REAL ESTATE") || dto.getKind().equalsIgnoreCase("DEVICE") ))
            return ResponseEntity.badRequest().body("Invalid kind given");
        permissionService.delete(dto);
        return ResponseEntity.ok().body("Success");
    }
}
