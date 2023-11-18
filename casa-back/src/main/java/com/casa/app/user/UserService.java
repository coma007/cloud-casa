package com.casa.app.user;

import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
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
}
