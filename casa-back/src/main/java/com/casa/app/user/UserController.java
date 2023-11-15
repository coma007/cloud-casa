package com.casa.app.user;

import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PermitAll
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id){
        User u = userService.getById(id);
        return new ResponseEntity<>(u.getUsername(), HttpStatus.OK);
    }

    @PermitAll
    @PostMapping
    public ResponseEntity<?> create(){
        User u = userService.createUser();
        return new ResponseEntity<>(u.getUsername(), HttpStatus.OK);
    }
}
