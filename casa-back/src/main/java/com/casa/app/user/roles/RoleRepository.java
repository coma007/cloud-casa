package com.casa.app.user.roles;

import com.casa.app.user.roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Long> {
    Optional<Role> getFirstByName(String name);
}
