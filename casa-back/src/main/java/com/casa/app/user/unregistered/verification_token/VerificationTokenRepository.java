package com.casa.app.user.unregistered.verification_token;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository  extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);

}
