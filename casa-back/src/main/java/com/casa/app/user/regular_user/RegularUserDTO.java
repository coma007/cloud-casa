package com.casa.app.user.regular_user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegularUserDTO {
    public String username;
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public MultipartFile file;

    public static RegularUser toModel(RegularUserDTO dto){
        RegularUser user = new RegularUser();
        user.setEmail(dto.email);
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        return user;
    }
}
