package com.casa.app.user.regular_user;

import com.casa.app.exceptions.NotFoundException;
import com.casa.app.user.UserRepository;
import com.casa.app.user.roles.Role;
import com.casa.app.user.roles.RoleRepository;
import com.casa.app.user.roles.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Optional;

@Service
public class RegularUserService {
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RegularUserRepository userRepository;

    public RegularUser register(RegularUserDTO dto) throws NotFoundException {
        RegularUser regularUser = RegularUserDTO.toModel(dto);
        MultipartFile file = dto.getFile();

        regularUser.setActive(false);

        Optional<Role> r = roleRepository.getFirstByName(Roles.regular);
        if(r.isEmpty()) throw new NotFoundException();
        regularUser.setRole(r.get());

        regularUser.setImageExtension(getExtension(Objects.requireNonNull(file.getOriginalFilename())));

        regularUser = userRepository.save(regularUser);
        return regularUser;

    }

    public String getExtension(String filename){
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}
