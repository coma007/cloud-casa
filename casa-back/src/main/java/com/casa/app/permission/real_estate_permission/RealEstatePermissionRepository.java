package com.casa.app.permission.real_estate_permission;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RealEstatePermissionRepository  extends JpaRepository<RealEstatePermission, Long> {

    @Query(value = "select" +
            " case when count(rep)> 0 " +
            " then true " +
            " else false end" +
            " from Device d" +
            " inner join d.realEstate re" +
            " inner join RealEstatePermission rep" +
            " on re.id = rep.realEstate.id " +
            " where d.id = :deviceId" +
            " and rep.user.id = :userId ")

    Boolean canAccess(long deviceId, long userId);
}
