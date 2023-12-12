package com.casa.app.user.admin;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.dtos.NewUserDTO;
import com.casa.app.user.dtos.UserDTO;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.user.roles.Roles;
import com.casa.app.util.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import org.hibernate.annotations.SecondaryRow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class AdminService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public EmailService emailService;

    @Transactional
    public UserDTO createAdmin(NewUserDTO userDTO) throws NotFoundException, MessagingException, UnsupportedEncodingException {
        Admin newUser = new Admin();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Optional<Role> adminRole = roleRepository.getFirstByName(Roles.admin);
        if(adminRole.isEmpty()) throw new NotFoundException();
        newUser.setRole(adminRole.get());

        adminRepository.save(newUser);

        emailService.sendPasswordEmail(newUser, newUser.getUsername(), userDTO.getPassword());
        return UserDTO.toDto(newUser);
    }
}
