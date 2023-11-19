package com.casa.app.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    User getFirstByUsername(String username);

    Optional<User> findByUsername(String username);
    Optional<User> findFirstByRoleName(String roleName);
}
