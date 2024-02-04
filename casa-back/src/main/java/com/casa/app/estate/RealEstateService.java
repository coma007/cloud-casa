package com.casa.app.estate;

import com.casa.app.device.Device;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.location.City;
import com.casa.app.location.LocationService;
import com.casa.app.permission.PermissionService;
import com.casa.app.permission.real_estate_permission.RealEstatePermission;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionService;
import com.casa.app.request.RealEstateRequest;
import com.casa.app.request.RealEstateRequestService;
import com.casa.app.user.User;
import com.casa.app.user.UserService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserRepository;
import com.casa.app.user.regular_user.RegularUserService;
import com.casa.app.util.email.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.casa.app.util.email.FileUtil.getExtension;

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
    @Autowired
    private PermissionService permissionService;

    public RealEstateDTO create(RealEstateCreateDTO estateDTO, MultipartFile multipartFile) throws UserNotFoundException, IOException {

        RegularUser currentUser = regularUserService.getUserByToken();

        City city = locationService.getByName(estateDTO.getCity());

        RealEstate estate = new RealEstate(estateDTO, city);
        estate.setImageExtension(getExtension(Objects.requireNonNull(multipartFile.getOriginalFilename())));
        estate = realEstateRepository.save(estate);

        estate.setRequest(realEstateRequestService.create(estate));
        realEstatePermissionService.createOwnershipPermission(currentUser, estate);

        File file = new File(FileUtil.estateDir + estate.getId().toString() + "." + estate.getImageExtension());
        try (OutputStream os = new FileOutputStream(file)) {
            os.write(multipartFile.getBytes());
        } catch (IOException e) {
            realEstateRepository.delete(estate);
            throw e;
        }

        return new RealEstateDTO(estate);
    }

    public List<RealEstateDTO> getAllByOwner() throws UserNotFoundException {

        RegularUser currentUser = regularUserService.getUserByToken();

        List<RealEstate> estates = realEstateRepository.getAllByOwnerUser(currentUser);
        return estates.stream().map(estate->new RealEstateDTO(estate)).collect(Collectors.toList());
    }

    public List<RealEstateDTO> getAllApprovedByOwner() throws UserNotFoundException {

        RegularUser currentUser = regularUserService.getUserByToken();

        List<RealEstate> estates = realEstateRepository.getAllByOwnerUser(currentUser);
        List<RealEstate> approvedEstates = new ArrayList<>();
        for (RealEstate e : estates) {
            if (e.getRequest().isApproved()) approvedEstates.add(e);
        }

        return approvedEstates.stream().map(RealEstateDTO::new).collect(Collectors.toList());
    }

    public List<RealEstateDTO> getAll() throws UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        List<RealEstate> estates = realEstateRepository.findAll();

        return estates.
                stream().
                parallel().
                filter(e-> permissionService.canReadEstate(e.getId(), currentUser.getId())).
                map(e -> new RealEstateDTO(e)).
                collect(Collectors.toList());
    }

    public List<RealEstateDTO> getAllApproved() throws UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        List<RealEstate> estates = realEstateRepository.findAll();

        return estates.
                stream().
                parallel().
                filter(e-> permissionService.canReadEstate(e.getId(), currentUser.getId()) && e.getRequest().isApproved()).
                map(e -> new RealEstateDTO(e)).
                collect(Collectors.toList());
    }

    public RealEstate getByName(String name) {
        return realEstateRepository.getByName(name);
    }

    public RealEstate getById(Long id) {
        return realEstateRepository.findById(id).orElse(null);
    }

    public Boolean isOwner(Long id) throws NotFoundException, UserNotFoundException {
        RealEstate estate = realEstateRepository.findById(id).orElse(null);
        if(estate == null)
            throw new NotFoundException();
        RegularUser user = regularUserService.getUserByToken();
        return estate.getOwner().getUser().getId().equals(user.getId());
    }
}
