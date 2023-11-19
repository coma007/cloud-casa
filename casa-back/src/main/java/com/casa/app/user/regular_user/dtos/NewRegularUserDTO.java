package com.casa.app.user.regular_user.dtos;

import com.casa.app.user.regular_user.RegularUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewRegularUserDTO {
    public String firstName;
    public String lastName;
    public String email;
    public String password;
    public MultipartFile file;

    public static RegularUser toModel(NewRegularUserDTO dto){
        RegularUser user = new RegularUser();
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        user.setUsername(dto.getEmail());
        user.setPassword(dto.getPassword());
        return user;
    }
}
