package com.casa.app.user.admin;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.dtos.NewUserDTO;
import com.casa.app.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('super admin')")
    public ResponseEntity<?> createNewAdmin(@RequestBody NewUserDTO newUserDTO){
        try {
            return ResponseEntity.ok(userService.createAdmin(newUserDTO));
        } catch (NotFoundException e) {
            return ResponseEntity.internalServerError().body("Could not find role");
        }
    }

}
