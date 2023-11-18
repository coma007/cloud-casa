package com.casa.app.permission.real_estate_permission;

import com.casa.app.estate.RealEstateDTO;
import com.casa.app.user.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RealEstatePermissionDTO {

    private User user; // TODO : change to dto
    private RealEstateDTO realEstate;
    private String type;

    public RealEstatePermissionDTO(RealEstatePermission permission) {
        this.user = permission.getUser();
        this.realEstate = new RealEstateDTO(permission.getRealEstate());
        this.type = permission.getType().name();
    }
}
