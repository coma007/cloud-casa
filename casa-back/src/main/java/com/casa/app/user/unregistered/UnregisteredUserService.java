package com.casa.app.user.unregistered;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.UserRepository;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserDTO;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.util.email.EmailService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
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

    public RegularUser register(RegularUserDTO userDTO) throws MessagingException, IOException, NotFoundException {
        RegularUser newUser = createNewRegularUser(userDTO);

        newUser = regularUserRepository.save(newUser);
        return newUser;
    }


    private RegularUser createNewRegularUser(RegularUserDTO userDTO) throws NotFoundException {
        RegularUser newPassenger = RegularUserDTO.toModel(userDTO);
        Optional<Role> regularUserRole = roleRepository.getFirstByName("regular user");
        if(regularUserRole.isEmpty()) throw new NotFoundException();
        newPassenger.setRole(regularUserRole.get());

//        String encodedPassword = passwordEncoder.encode(userDTO.password);
        String encodedPassword = userDTO.password;
        newPassenger.setPassword(encodedPassword);
        return newPassenger;
    }


//    @Transactional
    public boolean activate(Long activationId) throws NotFoundException {
        Optional<RegularUser> regularUserO = this.regularUserRepository.findById(activationId);
        if (regularUserO.isEmpty()) {
            throw new NotFoundException();
        }

        RegularUser regularUser = regularUserO.get();

        if (regularUser.isEnabled()) {
            return false;
        }
        regularUser.setActive(true);
        regularUserRepository.save(regularUser);

        return true;
    }

    public RegularUser findById(Long id) {
        return regularUserRepository.findById(id).orElse(null);
    }
}
