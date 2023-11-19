package com.casa.app.user.dtos;

import com.casa.app.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    public String username;
    public String role;

    public static UserDTO toDto(User user){
        UserDTO userDto = new UserDTO();
        userDto.setUsername(user.getUsername());
        userDto.setRole(user.getRole().getName());
        return userDto;
    }
}