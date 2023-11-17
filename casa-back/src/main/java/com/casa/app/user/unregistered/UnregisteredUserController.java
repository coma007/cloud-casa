package com.casa.app.user.unregistered;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserDTO;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.util.email.EmailService;
import jakarta.annotation.security.PermitAll;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api")
public class UnregisteredUserController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private RegularUserService regularUserService;

    @PermitAll
    @RequestMapping(path = "/register", method = POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> register(@ModelAttribute RegularUserDTO dto){
        RegularUser user = null;
        try {
            user = regularUserService.register(dto);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not save file");
        }
        return ResponseEntity.ok(user);
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
