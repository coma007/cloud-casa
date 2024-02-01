package com.casa.app.permission;


import com.casa.app.estate.RealEstateDTO;
import com.casa.app.estate.RealEstateService;
import com.casa.app.exceptions.NotFoundException;
import com.casa.app.exceptions.UserNotFoundException;
import com.casa.app.permission.device_permission.DevicePermission;
import com.casa.app.permission.device_permission.DevicePermissionRepository;
import com.casa.app.permission.dto.NewPermissionDTO;
import com.casa.app.permission.real_estate_permission.RealEstatePermission;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionRepository;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionService;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.casa.app.estate.RealEstate;

@Service
public class PermissionService {

    @Autowired
    DevicePermissionRepository devicePermissionRepository;

    @Autowired
    RealEstatePermissionRepository realEstatePermissionRepository;
    @Autowired
    private RegularUserService regularUserService;
    @Autowired
    private RealEstateService realEstateService;

    public boolean canReadDevice(long deviceId, long userId){
        return devicePermissionRepository.canAccess(deviceId, userId) ||
                realEstatePermissionRepository.canAccess(deviceId, userId);
    }

    public boolean canWriteDevice(long deviceId, long userId){
        return realEstatePermissionRepository.canAccess(deviceId, userId);
    }

    public PermissionType toType(String type) throws NotFoundException {
        if(type.equalsIgnoreCase("OWNER"))
            return PermissionType.OWNER;
        if(type.equalsIgnoreCase("MODERATOR"))
            return PermissionType.MODERATOR;
        throw new NotFoundException();
    }

    public NewPermissionDTO create(NewPermissionDTO dto) throws NotFoundException, UserNotFoundException {
        RegularUser user = regularUserService.getUserById(dto.getUserId());
        if(user == null)
            throw new UserNotFoundException();
        if(dto.getKind().equalsIgnoreCase("DEVICE")){
            DevicePermission permission = new DevicePermission();
            permission.setType(toType(dto.getType()));
            permission.setUserId(dto.getUserId());
            permission.setDeviceId(dto.getResourceId());
            devicePermissionRepository.save(permission);
        }
        else if(dto.getKind().equalsIgnoreCase("REAL ESTATE")){
            RealEstate realEstate = realEstateService.getById(dto.getResourceId());
            if (realEstate == null)
                throw new NotFoundException();
            RealEstatePermission permission = new RealEstatePermission();
            permission.setType(toType(dto.getType()));
            permission.setUser(user);
            permission.setRealEstate(realEstate);
            realEstatePermissionRepository.save(permission);
        }
        else throw new NotFoundException();
        return dto;
    }
}
