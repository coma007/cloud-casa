package com.casa.app.device.home.air_conditioning.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AirConditioningScheduleRepository extends JpaRepository<AirConditionSchedule, Long> {
    List<AirConditionSchedule> findByAirConditioning_Id(Long id);
}
