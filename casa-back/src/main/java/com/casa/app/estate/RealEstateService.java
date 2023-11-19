package com.casa.app.estate;

import com.casa.app.location.City;
import com.casa.app.location.LocationService;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionService;
import com.casa.app.request.RealEstateRequestService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RealEstateService {

    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    RegularUserRepository regularUserRepository; // todo change to service
    @Autowired
    LocationService locationService;
    @Autowired
    RealEstatePermissionService realEstatePermissionService;
    @Autowired
    RealEstateRequestService realEstateRequestService;

    public RealEstateDTO create(RealEstateCreateDTO estateDTO) {

        // TODO get user from session
        RegularUser currentUser = regularUserRepository.getById(Long.valueOf(2));

        City city = locationService.getByName(estateDTO.getCity().getName());
        RealEstate estate = new RealEstate(estateDTO, city);
        estate = realEstateRepository.save(estate);

        realEstatePermissionService.createOwnershipPermission(currentUser, estate);
        realEstateRequestService.createRequest(estate);

        return new RealEstateDTO(estate);
    }

    public List<RealEstateDTO> getAllByOwner() {

        // TODO get user from session
        RegularUser currentUser = regularUserRepository.getById(Long.valueOf(3));

        List<RealEstate> estates = realEstateRepository.getAllByOwnerUser(currentUser);
        return estates.stream().map(estate->new RealEstateDTO(estate)).collect(Collectors.toList());
    }

    public RealEstate getByName(String name) {
        return realEstateRepository.getByName(name);
    }
}
