package com.casa.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RoleRepository roleRepository;

    public User getById(Long id){
        return userRepository.getReferenceById(id);
    }

    public User createUser(){
        Optional<Role> r = roleRepository.getFirstByName("admin");
        return userRepository.save(new User(0L, "asd", "asd", r.get()));
    }
}
