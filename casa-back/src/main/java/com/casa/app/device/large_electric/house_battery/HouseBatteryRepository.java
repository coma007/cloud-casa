package com.casa.app.device.large_electric.house_battery;

import com.casa.app.estate.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HouseBatteryRepository extends JpaRepository<HouseBattery, Long> {

    public List<HouseBattery> findAllByRealEstate(RealEstate realEstate);
}
