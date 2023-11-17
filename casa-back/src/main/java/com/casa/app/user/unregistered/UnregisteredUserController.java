package com.casa.app.user.unregistered;

import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserDTO;
import com.casa.app.util.email.EmailService;
import jakarta.annotation.security.PermitAll;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
public class UnregisteredUserController {

    @Autowired
    private EmailService emailService;

    @PermitAll
    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegularUserDTO dto){
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PermitAll
    @GetMapping
    public ResponseEntity<?> sendEmail(){
        RegularUser u = new RegularUser();
        u.setPassword("asd");
        u.setLastName("peric");
        u.setFirstName("pera");
        u.setEmail("nemanja.majstorovic3214@gmail.com");
        try {
            emailService.sendVerificationEmail(u, "Asd");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return new ResponseEntity<>(u.getUsername(), HttpStatus.OK);
    }
}
