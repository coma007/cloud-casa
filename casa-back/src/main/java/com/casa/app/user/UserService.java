package com.casa.app.user;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.admin.Admin;
import com.casa.app.user.dtos.NewUserDTO;
import com.casa.app.user.dtos.UserDTO;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.user.roles.Roles;
import com.casa.app.util.email.EmailService;
import com.casa.app.util.email.JWTUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RegularUserRepository regularUserRepository;

    @Autowired
    public RoleRepository roleRepository;

    @Autowired
    public EmailService emailService;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserDTO getById(Long id){
        return UserDTO.toDto(userRepository.getReferenceById(id));
    }

    public User getUserByToken(){
        SecurityContext context = SecurityContextHolder.getContext();
        User user = (User) context.getAuthentication().getPrincipal();
        return userRepository.getReferenceById(user.getId());
    }

    public UserDTO createAdmin(NewUserDTO userDTO) throws NotFoundException, MessagingException, UnsupportedEncodingException {
        Admin newUser = new Admin();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        Optional<Role> adminRole = roleRepository.getFirstByName(Roles.admin);
        if(adminRole.isEmpty()) throw new NotFoundException();
        newUser.setRole(adminRole.get());

        userRepository.save(newUser);

        emailService.sendPasswordEmail(newUser, newUser.getUsername(), userDTO.getPassword());
        return UserDTO.toDto(newUser);
    }

}
