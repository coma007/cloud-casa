package com.casa.app.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    public UserRepository userRepository;

    public User getById(Long id){
        return userRepository.getReferenceById(id);
    }

    public User createUser(){
        return userRepository.save(new User(0L, "asd", "asd", "asd"));
    }
}
