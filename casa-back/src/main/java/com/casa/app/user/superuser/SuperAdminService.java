package com.casa.app.user.superuser;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.User;
import com.casa.app.user.UserService;
import com.casa.app.user.roles.Role;
import com.casa.app.user.UserRepository;
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
import java.nio.file.NotDirectoryException;
import java.util.Optional;

@Service
public class SuperAdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Autowired
    private SuperAdminRepository superAdminRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void createSuperUserPassword() throws NotFoundException {
        Optional<User> userInitOptional = userRepository.findFirstByRoleName(Roles.superAdminInit);
        Optional<User> userOptional = userRepository.findFirstByRoleName(Roles.superAdmin);
        if(userOptional.isEmpty() && userInitOptional.isEmpty()){
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
            Optional<Role> superadminRoleO = roleRepository.getFirstByName(Roles.superAdminInit);
            if(superadminRoleO.isEmpty()) throw new NotFoundException();

            SuperAdmin newSuperadmin = new SuperAdmin();
            newSuperadmin.setPassword(encoder.encode(password));
            newSuperadmin.setRole(superadminRoleO.get());
            newSuperadmin.setUsername("admin");
            newSuperadmin.setInit(true);

            superAdminRepository.save(newSuperadmin);

        }
    }

    public boolean isInit() throws NotFoundException {
        User tokenUser = userService.getUserByToken();
        Optional<SuperAdmin> userO = superAdminRepository.findByUsername(tokenUser.getUsername());
        if(userO.isEmpty()) throw new NotFoundException();
        return userO.get().isInit();
    }
}
