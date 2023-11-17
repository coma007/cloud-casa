package com.casa.app.device;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConnectionCredentialsRepository extends JpaRepository<ConnectionCredentials, Long> {
}
