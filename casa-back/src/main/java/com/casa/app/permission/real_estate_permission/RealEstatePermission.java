package com.casa.app.permission.real_estate_permission;

import com.casa.app.permission.PermissionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(RealEstatePermissionKey.class)
public class RealEstatePermission {
    @Id
    private long userId;

    @Id
    private long realEstateId;

    private PermissionType type;
}
