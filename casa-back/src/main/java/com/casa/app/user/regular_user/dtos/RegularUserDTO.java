package com.casa.app.user.regular_user.dtos;

import com.casa.app.user.regular_user.RegularUser;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegularUserDTO {
    public String firstName;
    public String lastName;
    public String email;

    public static RegularUserDTO toDto(RegularUser user){
        return new RegularUserDTO(user.getFirstName(), user.getLastName(), user.getUsername());
    }
}