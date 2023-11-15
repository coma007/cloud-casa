package com.casa.app.permission.device_permission;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class DevicePermissionKey implements Serializable {
    private long userId;
    private long deviceId;
}
