package com.casa.app.estate;

import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.location.City;
import com.casa.app.location.LocationService;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionService;
import com.casa.app.request.RealEstateRequest;
import com.casa.app.request.RealEstateRequestService;
import com.casa.app.user.User;
import com.casa.app.user.UserService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.regular_user.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RealEstateService {

    @Autowired
    RealEstateRepository realEstateRepository;
    @Autowired
    RegularUserService regularUserService;
    @Autowired
    LocationService locationService;
    @Autowired
    RealEstateRequestService realEstateRequestService;
    @Autowired
    RealEstatePermissionService realEstatePermissionService;

    public RealEstateDTO create(RealEstateCreateDTO estateDTO) throws UserNotFoundException {

        RegularUser currentUser = regularUserService.getUserByToken();

        City city = locationService.getByName(estateDTO.getCity());

        RealEstate estate = new RealEstate(estateDTO, city);
        estate = realEstateRepository.save(estate);

        estate.setRequest(realEstateRequestService.create(estate));
        realEstatePermissionService.createOwnershipPermission(currentUser, estate);

        return new RealEstateDTO(estate);
    }

    public List<RealEstateDTO> getAllByOwner() throws UserNotFoundException {

        RegularUser currentUser = regularUserService.getUserByToken();

        List<RealEstate> estates = realEstateRepository.getAllByOwnerUser(currentUser);
        return estates.stream().map(estate->new RealEstateDTO(estate)).collect(Collectors.toList());
    }

    public RealEstate getByName(String name) {
        return realEstateRepository.getByName(name);
    }
}
