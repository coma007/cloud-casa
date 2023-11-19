package com.casa.app.user.superuser;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.User;
import com.casa.app.user.roles.Role;
import com.casa.app.user.UserRepository;
import com.casa.app.user.UserService;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.user.roles.Roles;
import com.casa.app.util.email.FileUtil;
import com.casa.app.util.email.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class SuperUserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @EventListener(ApplicationReadyEvent.class)
    public void createSuperUserPassword() throws NotFoundException {
        Optional<User> userOptional = userRepository.findFirstByRoleName(Roles.superAdmin);
        if(userOptional.isEmpty()){
            FileUtil.createIfNotExists(FileUtil.pwdDir);
            String password = Random.makeRandomString(64);

            try {
                Writer writer = new BufferedWriter(new OutputStreamWriter(
                        new FileOutputStream(FileUtil.pwdDir + "pwd.txt"), "utf-8"));
                writer.write(password);
                writer.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Optional<Role> superadminRoleO = roleRepository.getFirstByName(Roles.superAdmin);
            if(superadminRoleO.isEmpty()) throw new NotFoundException();

            User newSuperadmin = new User();
            newSuperadmin.setPassword(encoder.encode(password));
            newSuperadmin.setRole(superadminRoleO.get());
            newSuperadmin.setUsername("admin");

            userRepository.save(newSuperadmin);

        }
    }
}
