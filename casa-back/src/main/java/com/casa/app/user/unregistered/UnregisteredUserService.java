package com.casa.app.user.unregistered;

import com.casa.app.exceptions.InvalidTokenException;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.UserRepository;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.dtos.NewRegularUserDTO;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.regular_user.dtos.RegularUserDTO;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.user.roles.Roles;
import com.casa.app.user.unregistered.verification_token.VerificationToken;
import com.casa.app.user.unregistered.verification_token.VerificationTokenRepository;
import com.casa.app.util.email.EmailService;
import com.casa.app.util.email.FileUtil;
import com.casa.app.util.email.JWTUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;

@Service
public class UnregisteredUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegularUserRepository regularUserRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Value("${server.ip}")
    private String IP;
    @Value("${server.port}")
    private int PORT;

    @Autowired
    private PasswordEncoder encoder;

    public RegularUserDTO register(NewRegularUserDTO dto) throws NotFoundException, IOException {
        dto.setPassword(encoder.encode(dto.getPassword()));
        RegularUser regularUser = NewRegularUserDTO.toModel(dto);
        MultipartFile multipartFile = dto.getFile();

        regularUser.setActive(false);

        Optional<Role> r = roleRepository.getFirstByName(Roles.regular);
        if(r.isEmpty()) throw new NotFoundException();
        regularUser.setRole(r.get());

        regularUser.setImageExtension(getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));

        VerificationToken token = generateToken();
        token.setRegularUser(regularUser);

        regularUser = userRepository.save(regularUser);
        verificationTokenRepository.save(token);

        File file = new File(FileUtil.imagesDir + regularUser.getId().toString() + "." + regularUser.getImageExtension());
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            userRepository.delete(regularUser);
            throw e;
        }

        try {
                emailService.sendVerificationEmail(regularUser, "http://" + IP + ":" + PORT + "/api/verify?token=" + token.getToken());
            } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return RegularUserDTO.toDto(regularUser);

    }

    public String getExtension(String filename){
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private VerificationToken generateToken() {
        VerificationToken token = new VerificationToken();

        String randomCode = makeRandomString(64);
        token.setToken(randomCode);

        return token;

    }

    private String makeRandomString(int i) {
        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[128];
        random.nextBytes(bytes);
        Base64.Encoder encoder = Base64.getUrlEncoder().withoutPadding();
        String token = encoder.encodeToString(bytes);
        return token;
    }

    public boolean verify(String verificationCode) throws InvalidTokenException, NotFoundException {
        Optional<VerificationToken> tokenO = verificationTokenRepository.findByToken(verificationCode);
        if(tokenO.isEmpty()) throw new InvalidTokenException();
        RegularUser regularUser = tokenO.get().getRegularUser();
        return activate(regularUser.getId());
    }

    public boolean activate(Long activationId) throws NotFoundException {
        Optional<RegularUser> regularUserO = regularUserRepository.findById(activationId);
        if (regularUserO.isEmpty()) {
            throw new NotFoundException();
        }

        RegularUser regularUser = regularUserO.get();

        if (regularUser.isActive()) {
            return false;
        }

        regularUser.setActive(true);
        regularUser.setJwt(jwtUtil.generateToken(regularUser.getId(), regularUser.getEmail(), null));
        regularUserRepository.save(regularUser);

        return true;
    }

    public RegularUser findById(Long id) {
        return regularUserRepository.findById(id).orElse(null);
    }
}
