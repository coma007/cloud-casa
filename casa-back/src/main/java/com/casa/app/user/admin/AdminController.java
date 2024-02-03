package com.casa.app.user.admin;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.dtos.NewUserDTO;
import com.casa.app.user.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;

    @PostMapping
    @PreAuthorize("hasAnyAuthority('super admin')")
    public ResponseEntity<?> createNewAdmin(@RequestBody NewUserDTO newUserDTO){
        try {
            return ResponseEntity.ok(adminService.createAdmin(newUserDTO));
        } catch (NotFoundException e) {
            return ResponseEntity.internalServerError().body("Could not find role");
        } catch (MessagingException e) {
            return ResponseEntity.internalServerError().body("Could not send email");
        } catch (UnsupportedEncodingException e) {
            return ResponseEntity.internalServerError().body("Unsupported encoding");
        } catch (IOException e) {
            return ResponseEntity.internalServerError().body("Email could not be sent");
        }
    }

}
