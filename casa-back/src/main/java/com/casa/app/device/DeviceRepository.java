package com.casa.app.device;

import com.casa.app.estate.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    public Device getDeviceByName(String name);

    public List<Device> findAllByRealEstate(RealEstate realEstate);
}
