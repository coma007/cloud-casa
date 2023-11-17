package com.casa.app.user;

import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.util.email.EmailService;
import jakarta.annotation.security.PermitAll;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

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

}
