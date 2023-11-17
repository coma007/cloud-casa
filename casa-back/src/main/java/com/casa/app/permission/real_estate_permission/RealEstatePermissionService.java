package com.casa.app.permission.real_estate_permission;

import com.casa.app.estate.RealEstate;
import com.casa.app.estate.RealEstateRepository;
import com.casa.app.permission.PermissionType;
import com.casa.app.user.regular_user.RegularUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealEstatePermissionService {

    @Autowired
    RealEstatePermissionRepository realEstatePermissionRepository;
    @Autowired
    RealEstateRepository realEstateRepository;


    public RealEstatePermissionDTO createOwnershipPermission(RegularUser user, RealEstate estate) {

        RealEstatePermission permission = new RealEstatePermission(user, estate, PermissionType.OWNER);
        permission = realEstatePermissionRepository.save(permission);
        estate.setOwner(permission);
        realEstateRepository.save(estate);
        return new RealEstatePermissionDTO(permission);
    }
}
