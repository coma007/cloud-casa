package com.casa.app.user;

import com.casa.app.exceptions.InvalidCredentialsException;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.dtos.NewPasswordDTO;
import com.casa.app.user.dtos.UserDTO;
import com.casa.app.user.superuser.SuperAdminService;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private SuperAdminService superAdminService;

//    Conflicting with /init
//    @PermitAll
//    @GetMapping("/{id}")
//    public ResponseEntity<?> getById(@PathVariable Long id){
//        UserDTO u = userService.getById(id);
//        return new ResponseEntity<>(u, HttpStatus.OK);
//    }

    @PutMapping("/change-password")
    @PreAuthorize("hasAnyAuthority('super admin', 'admin', 'regular user')")
    public ResponseEntity<?> changePassword(@RequestBody NewPasswordDTO dto){
        try {
            userService.changePassword(dto);
            return ResponseEntity.ok().build();
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.badRequest().body("Wrong credentials");
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("Cannot find user");
        }
    }

    @GetMapping("/init")
    @PreAuthorize("hasAnyAuthority('super admin')")
    public ResponseEntity<?> isInit(){
        try {
            boolean result = superAdminService.isInit();
            return ResponseEntity.ok().body(result);
        } catch (NotFoundException e) {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

}
