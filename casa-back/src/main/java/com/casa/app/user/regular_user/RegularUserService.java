package com.casa.app.user.regular_user;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.UserRepository;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.user.roles.Roles;
import com.casa.app.util.email.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.Optional;

@Service
public class RegularUserService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RegularUserRepository userRepository;

    public RegularUser register(RegularUserDTO dto) throws NotFoundException, IOException {
        RegularUser regularUser = RegularUserDTO.toModel(dto);
        MultipartFile multipartFile = dto.getFile();

        regularUser.setActive(false);

        Optional<Role> r = roleRepository.getFirstByName(Roles.regular);
        if(r.isEmpty()) throw new NotFoundException();
        regularUser.setRole(r.get());

        regularUser.setImageExtension(getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));

        regularUser = userRepository.save(regularUser);

//        TODO move this to init
        FileUtil.createImagesIfNotExists();
        File file = new File(FileUtil.imagesDir + multipartFile.getOriginalFilename());
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            userRepository.delete(regularUser);
            throw e;
        }
        return regularUser;

    }

    public String getExtension(String filename){
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
