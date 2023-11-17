package com.casa.app.user.regular_user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegularUserDTO {
    public String firstName;
    public String lastName;
    public String email;
    public String password;

    public static RegularUser toModel(RegularUserDTO dto){
        RegularUser user = new RegularUser();
        user.setEmail(dto.email);
        user.setFirstName(dto.firstName);
        user.setLastName(dto.lastName);
        return user;
    }
}
