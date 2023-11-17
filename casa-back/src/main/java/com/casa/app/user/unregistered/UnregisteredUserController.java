package com.casa.app.user.unregistered;

import com.casa.app.exceptions.InvalidTokenException;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserDTO;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.user.unregistered.verification_token.VerificationTokenRepository;
import com.casa.app.util.email.EmailService;
import jakarta.annotation.security.PermitAll;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/api")
public class UnregisteredUserController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private RegularUserService regularUserService;

    @Autowired
    private UnregisteredUserService unregisteredUserService;

    @Value("${server.ip}")
    private String IP;
    @Value("${server.port}")
    private int PORT;

    @PermitAll
    @RequestMapping(path = "/register", method = POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> register(@ModelAttribute RegularUserDTO dto){
        RegularUser user = null;
        try {
            user = unregisteredUserService.register(dto);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Could not save file");
        }
        return ResponseEntity.ok(user);
    }

    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@RequestParam("token") String code) {
        boolean verified = false;
        try {
            verified = unregisteredUserService.verify(code);
        } catch (InvalidTokenException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid token passed");
        } catch (NotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Cannot find user or token");
        }

        URI yahoo = null;
        HttpHeaders httpHeaders = new HttpHeaders();
        if (verified) {

            try {
                yahoo = new URI("http://" + IP + ":" + PORT + "/login");
            } catch (URISyntaxException e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Could not parse uri");
            }

            httpHeaders.setLocation(yahoo);
            return new ResponseEntity<>(httpHeaders, HttpStatus.OK);

        } else {

            try {
                yahoo = new URI("http://" + IP + ":" + PORT + "/bad-request");
            } catch (URISyntaxException e) {
                return ResponseEntity
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Could not parse uri");
            }

//            httpHeaders.setLocation(yahoo);
            return new ResponseEntity<>(httpHeaders, HttpStatus.NOT_ACCEPTABLE);
        }
    }
}
