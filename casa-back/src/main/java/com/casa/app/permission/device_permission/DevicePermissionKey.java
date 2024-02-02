package com.casa.app.permission.device_permission;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DevicePermissionKey implements Serializable {
    private long userId;
    private long deviceId;
}
