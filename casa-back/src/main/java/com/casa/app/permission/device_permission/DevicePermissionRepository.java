package com.casa.app.permission.device_permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DevicePermissionRepository extends JpaRepository<DevicePermission, Long> {

    @Query(value = "select" +
            " case when count(dp)> 0 " +
            " then true " +
            " else false end" +
            " from DevicePermission dp" +
            " where dp.deviceId = :deviceId" +
            " and dp.userId = :userId ")

    Boolean canAccess(long deviceId, long userId);


}
