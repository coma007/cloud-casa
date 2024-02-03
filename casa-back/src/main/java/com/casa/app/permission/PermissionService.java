package com.casa.app.permission;


import com.casa.app.device.Device;
import com.casa.app.device.DeviceRepository;
import com.casa.app.device.DeviceService;
import com.casa.app.estate.RealEstateService;
import com.casa.app.exceptions.*;
import com.casa.app.permission.device_permission.DevicePermission;
import com.casa.app.permission.device_permission.DevicePermissionKey;
import com.casa.app.permission.device_permission.DevicePermissionRepository;
import com.casa.app.permission.dto.PermissionDTO;
import com.casa.app.permission.real_estate_permission.RealEstatePermission;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionRepository;
import com.casa.app.user.regular_user.RegularUser;
import com.casa.app.user.regular_user.RegularUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.casa.app.estate.RealEstate;

import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private DeviceRepository deviceRepository;

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

    public PermissionDTO create(PermissionDTO dto) throws NotFoundException, UserNotFoundException, UnauthorizedWriteException, AlreadyExistsException {
        RegularUser user = regularUserService.getUserById(dto.getUserId());
        if(user == null)
            throw new UserNotFoundException();

        if(dto.getKind().equalsIgnoreCase("DEVICE")){
            Device device = deviceRepository.findById(dto.getResourceId()).orElse(null);
            if(device == null)
                throw new NotFoundException();
            RegularUser currentUser = regularUserService.getUserByToken();
            if(!device.getOwner().getId().equals(currentUser.getId()))
                throw new UnauthorizedWriteException();

            if(devicePermissionRepository.findByIds(dto.getResourceId(), dto.getUserId()) != null)
                throw new AlreadyExistsException();
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
            RegularUser currentUser = regularUserService.getUserByToken();
            if(!realEstate.getOwner().getUser().getId().equals(currentUser.getId()))
                throw new UnauthorizedWriteException();

            RealEstatePermission permission = new RealEstatePermission();
            permission.setType(toType(dto.getType()));
            permission.setUser(user);
            permission.setRealEstate(realEstate);
            realEstatePermissionRepository.save(permission);
        }
        else throw new NotFoundException();
        return dto;
    }

    public List<Device> filterReadDevices(List<Device> devices) throws UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        return devices
                .parallelStream()
                .filter(d->canReadDevice(d.getId(), currentUser.getId()))
                .collect(Collectors.toList());
    }

    public List<Device> filterWriteDevices(List<Device> devices) throws UserNotFoundException {
        RegularUser currentUser = regularUserService.getUserByToken();
        return devices
                .parallelStream()
                .filter(d->canWriteDevice(d.getId(), currentUser.getId()))
                .collect(Collectors.toList());
    }

    public void canRead(long deviceId) throws UserNotFoundException, UnathorizedReadException {
        RegularUser currentUser = regularUserService.getUserByToken();
        if(!canReadDevice(deviceId, currentUser.getId()))
           throw new UnathorizedReadException();

    }

    public void canWrite(long deviceId) throws UserNotFoundException, UnauthorizedWriteException {
        RegularUser currentUser = regularUserService.getUserByToken();
        if(!canWriteDevice(deviceId, currentUser.getId()))
            throw new UnauthorizedWriteException();

    }

    public void delete(PermissionDTO dto) throws UserNotFoundException, NotFoundException, UnauthorizedWriteException {
        RegularUser user = regularUserService.getUserById(dto.getUserId());
        if(user == null)
            throw new UserNotFoundException();
        if(dto.getKind().equalsIgnoreCase("DEVICE")){
            Device device = deviceRepository.findById(dto.getResourceId()).orElse(null);
            if(device == null)
                throw new NotFoundException();
            RegularUser currentUser = regularUserService.getUserByToken();
            if(!device.getOwner().getId().equals(currentUser.getId()))
                throw new UnauthorizedWriteException();

            DevicePermission permission = devicePermissionRepository.findByIds(dto.getResourceId(), dto.getUserId());
            if(permission == null)
                throw new NotFoundException();
            devicePermissionRepository.delete(permission);
        }
        else if(dto.getKind().equalsIgnoreCase("REAL ESTATE")){
            RealEstate realEstate = realEstateService.getById(dto.getResourceId());
            if (realEstate == null)
                throw new NotFoundException();
            RegularUser currentUser = regularUserService.getUserByToken();
            if(!realEstate.getOwner().getUser().getId().equals(currentUser.getId()))
                throw new UnauthorizedWriteException();

            RealEstatePermission permission = realEstatePermissionRepository.findByIds(dto.getResourceId(), dto.getUserId());
            if(permission==null)
                throw new NotFoundException();
            realEstatePermissionRepository.delete(permission);
        }
        else throw new NotFoundException();
    }
}
