package com.casa.app.user;

import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
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

    public User getById(Long id){
        return userRepository.getReferenceById(id);
    }

    public RegularUser createRegularUser(){
        Optional<Role> r = roleRepository.getFirstByName("admin");
        return regularUserRepository.save(new RegularUser(0L, "asd", "asd", r.get(), "asd", "Asd", "asd", true, "asd"));
    }
}
