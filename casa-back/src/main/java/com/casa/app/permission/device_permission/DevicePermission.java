package com.casa.app.permission.device_permission;

import com.casa.app.permission.PermissionType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@IdClass(DevicePermissionKey.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class DevicePermission {
    @Id
    private long userId;

    @Id
    private long deviceId;

    private PermissionType type;
}
