package com.casa.app.user.unregistered.verification_token;
import java.time.LocalDateTime;

import com.casa.app.user.regular_user.RegularUser;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    @OneToOne(targetEntity = RegularUser.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "regular_user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RegularUser regularUser;

}