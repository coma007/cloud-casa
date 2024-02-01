package com.casa.app.device.large_electric.solar_panel_system;

import com.casa.app.estate.RealEstate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolarPanelSystemRepository extends JpaRepository<SolarPanelSystem, Long> {
    public List<SolarPanelSystem> findAllByRealEstate(RealEstate realEstate);
}
