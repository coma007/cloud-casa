package com.casa.app.device.home.washing_machine.schedule;

import com.casa.app.device.home.air_conditioning.schedule.AirConditionSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WashingMachineScheduleRepository extends JpaRepository<WashingMachineSchedule, Long> {
    List<WashingMachineSchedule> findByWashingMachine_Id(Long id);
}
