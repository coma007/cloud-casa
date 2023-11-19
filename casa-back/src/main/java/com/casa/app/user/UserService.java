package com.casa.app.user;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.dtos.NewUserDTO;
import com.casa.app.user.dtos.UserDTO;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.user.roles.Roles;
import com.casa.app.util.email.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private JWTUtil jwtUtil;

    public UserDTO getById(Long id){
        return UserDTO.toDto(userRepository.getReferenceById(id));
    }
    public User getUserByToken(String token){
        String username = jwtUtil.getUsernameFromToken(token);
        return userRepository.getFirstByUsername(username);
    }

    public UserDTO createAdmin(NewUserDTO userDTO) throws NotFoundException {
        User newUser = new User();
        newUser.setUsername(userDTO.getUsername());
        newUser.setPassword(userDTO.getPassword());
        Optional<Role> adminRole = roleRepository.getFirstByName(Roles.admin);
        if(adminRole.isEmpty()) throw new NotFoundException();
        newUser.setRole(adminRole.get());

        userRepository.save(newUser);
        return UserDTO.toDto(newUser);
    }
}
