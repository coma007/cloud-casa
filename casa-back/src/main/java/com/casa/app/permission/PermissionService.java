package com.casa.app.permission;


import com.casa.app.permission.device_permission.DevicePermissionRepository;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionRepository;
import com.casa.app.permission.real_estate_permission.RealEstatePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

    @Autowired
    DevicePermissionRepository devicePermissionRepository;

    @Autowired
    RealEstatePermissionRepository realEstatePermissionRepository;

    public boolean canReadDevice(long deviceId, long userId){
        return devicePermissionRepository.canAccess(deviceId, userId) ||
                realEstatePermissionRepository.canAccess(deviceId, userId);
    }

    public boolean canWriteDevice(long deviceId, long userId){
        return realEstatePermissionRepository.canAccess(deviceId, userId);
    }
}
