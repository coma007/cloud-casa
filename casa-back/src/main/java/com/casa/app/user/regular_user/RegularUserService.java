package com.casa.app.user.regular_user;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.user.User;
import com.casa.app.user.UserRepository;
import com.casa.app.user.dtos.UserDTO;
import com.casa.app.user.regular_user.dtos.RegularUserDTO;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.user.roles.Roles;
import com.casa.app.util.email.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegularUserService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RegularUserRepository regularUserRepository;

    public RegularUser getUserByToken() throws UserNotFoundException {
        SecurityContext context = SecurityContextHolder.getContext();
        User user = (User) context.getAuthentication().getPrincipal();
        return regularUserRepository.findById(user.getId()).orElseThrow(UserNotFoundException::new);
    }

    public RegularUser getUserById(long id){
        return regularUserRepository.findById(id).orElse(null);
    }
    public List<RegularUserDTO> getAll(){
        return regularUserRepository.findAll().stream().map(RegularUserDTO::toDto).collect(Collectors.toList());
    }
}
