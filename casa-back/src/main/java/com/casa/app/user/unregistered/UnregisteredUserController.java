package com.casa.app.user.unregistered;

import com.casa.app.exceptions.InvalidTokenException;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.User;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.dtos.NewRegularUserDTO;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.user.regular_user.dtos.RegularUserDTO;
import com.casa.app.util.email.EmailService;
import com.casa.app.util.email.JWTUtil;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
    private AuthenticationManager authenticationManager;

    @Autowired
    private UnregisteredUserService unregisteredUserService;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${server.ip}")
    private String IP;
    @Value("${server.port}")
    private int PORT;

    @PermitAll
    @RequestMapping(path = "/register", method = POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> register(@ModelAttribute NewRegularUserDTO dto){
        RegularUserDTO user = null;
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

    @RequestMapping(path = "/login", method = POST)
    public ResponseEntity<?> login(@RequestBody CredentialsDTO dto){
        UsernamePasswordAuthenticationToken authReq = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

        Authentication auth = null;
        try {
            auth = authenticationManager.authenticate(authReq);
        } catch (BadCredentialsException e) {
            throw e;
//            return ResponseEntity.badRequest().body("Wrong credentials");
        }catch (DisabledException e) {
            return ResponseEntity.badRequest().body("User is disabled!");
        }

        SecurityContext sc = SecurityContextHolder.getContext();
        sc.setAuthentication(auth);
        long id = ((User) auth.getPrincipal()).getId();
        String token = jwtUtil.generateToken(dto.getUsername(), auth.getAuthorities());
        return ResponseEntity.ok(token);
    }
}
