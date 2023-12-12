package com.casa.app.user.superuser;

import com.casa.app.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SuperAdmin extends User {
    private boolean init;
}
