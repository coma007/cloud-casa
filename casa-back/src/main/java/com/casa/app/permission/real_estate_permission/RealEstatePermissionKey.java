package com.casa.app.permission.real_estate_permission;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class RealEstatePermissionKey implements Serializable {
    private long userId;
    private long realEstateId;
}
