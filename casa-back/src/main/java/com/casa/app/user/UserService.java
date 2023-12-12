package com.casa.app.user;

import com.casa.app.exceptions.InvalidCredentialsException;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.admin.Admin;
import com.casa.app.user.dtos.NewPasswordDTO;
import com.casa.app.user.dtos.NewUserDTO;
import com.casa.app.user.dtos.UserDTO;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.user.roles.Roles;
import com.casa.app.user.superuser.SuperAdmin;
import com.casa.app.user.superuser.SuperAdminRepository;
import com.casa.app.user.superuser.SuperAdminService;
import com.casa.app.util.email.EmailService;
import com.casa.app.util.email.JWTUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RegularUserRepository regularUserRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @Autowired
    private RoleRepository roleRepository;

    public UserDTO getById(Long id){
        return UserDTO.toDto(userRepository.getReferenceById(id));
    }

    public User getUserByToken(){
        SecurityContext context = SecurityContextHolder.getContext();
        User user = (User) context.getAuthentication().getPrincipal();
        return userRepository.getReferenceById(user.getId());
    }


    public void changePassword(NewPasswordDTO dto) throws InvalidCredentialsException, NotFoundException {
        User tokenUser = getUserByToken();
        if(!passwordEncoder.matches(dto.getOldPassword(), tokenUser.getPassword()))
            throw new InvalidCredentialsException();
        tokenUser.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(tokenUser);
        if(Objects.equals(tokenUser.getRole().getName(), Roles.superAdminInit)){
            Optional<SuperAdmin> superAdminO = superAdminRepository.findByUsername(tokenUser.getUsername());
            if(superAdminO.isEmpty()) throw new NotFoundException();
            superAdminO.get().setInit(false);

            Optional<Role> superadminRoleO = roleRepository.getFirstByName(Roles.superAdmin);
            if(superadminRoleO.isEmpty()) throw new NotFoundException();
            superAdminO.get().setRole(superadminRoleO.get());

            superAdminRepository.save(superAdminO.get());
        }
    }
}
