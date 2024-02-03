package com.casa.app.user.regular_user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RegularUserRepository extends JpaRepository<RegularUser, Long> {
    RegularUser findByUsername(String username);
}
