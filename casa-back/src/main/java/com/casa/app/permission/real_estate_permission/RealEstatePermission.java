package com.casa.app.permission.real_estate_permission;

import com.casa.app.estate.RealEstate;
import com.casa.app.permission.PermissionType;
import com.casa.app.user.regular_user.RegularUser;
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
    @ManyToOne
    @JoinColumn(name = "user_id")
    private RegularUser user;

    @Id
    @ManyToOne
    @JoinColumn(name = "real_estate_id")
    private RealEstate realEstate;

    private PermissionType type;
}
