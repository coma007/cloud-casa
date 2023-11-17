package com.casa.app.estates;

import com.casa.app.estates.request.RealEstateRequest;
import com.casa.app.estates.request.RealEstateRequestRepository;
import com.casa.app.permission.PermissionType;
import com.casa.app.permission.real_estate_permission.RealEstatePermission;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionRepository;
import com.casa.app.user.User;
import com.casa.app.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RealEstateService {

    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RealEstatePermissionRepository realEstatePermissionRepository;
    @Autowired
    RealEstateRequestRepository realEstateRequestRepository;

    public RealEstateDTO create(RealEstateCreateDTO estateDTO) {

        // TODO get user from session
        User currentUser = userRepository.getById(Long.valueOf(3));
        RealEstate estate = new RealEstate(estateDTO);
        RealEstatePermission permission = new RealEstatePermission(currentUser.getId(), estate.getId(), PermissionType.OWNER);
        RealEstateRequest request = new RealEstateRequest(estate);

        estate = realEstateRepository.save(estate);
        permission = realEstatePermissionRepository.save(permission);
        request = realEstateRequestRepository.save(request);
        
        return new RealEstateDTO(estate, permission);
    }
}
