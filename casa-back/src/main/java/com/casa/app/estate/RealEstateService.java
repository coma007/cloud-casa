package com.casa.app.estate;

import com.casa.app.permission.real_estate_permission.RealEstatePermissionService;
import com.casa.app.request.RealEstateRequestService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RealEstateService {

    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    RegularUserRepository regularUserRepository; // todo change to service
    @Autowired
    RealEstatePermissionService realEstatePermissionService;
    @Autowired
    RealEstateRequestService realEstateRequestService;

    public RealEstateDTO create(RealEstateCreateDTO estateDTO) {

        // TODO get user from session
        RegularUser currentUser = regularUserRepository.getById(Long.valueOf(3));

        RealEstate estate = new RealEstate(estateDTO);
        estate = realEstateRepository.save(estate);

        realEstatePermissionService.createOwnershipPermission(currentUser, estate);
        realEstateRequestService.createRequest(currentUser, estate);

        return new RealEstateDTO(estate, currentUser);
    }
}
