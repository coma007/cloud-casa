package com.casa.app.user.regular_user;

import com.casa.app.user.User;
import com.casa.app.user.roles.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class RegularUser extends User {
    public String firstName;
    public String lastName;
    public String email;
    public boolean active;
    public String imageExtension;

    public RegularUser(Long id, String username, String password, Role role, String jwt, String firstName, String lastName, String email, boolean active, String imageExtension) {
        super(id, username, password, role, jwt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.active = active;
        this.imageExtension = imageExtension;
    }
}
