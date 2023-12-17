package com.casa.app.device.home.air_conditioning.schedule;

import com.casa.app.device.home.air_conditioning.AirConditioning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AirConditionScheduleRepository extends JpaRepository<AirConditionSchedule, Long> {
    @Query(value = "select" +
            " case when count(acs)> 0 " +
            " then true " +
            " else false end" +
            " from AirConditionSchedule acs" +
            " where acs.airConditioning.id = :deviceId" +
            " and ( :startDate1 <= acs.endTime AND acs.startTime <= :endDate1)" +
            " and acs.override = false ")
    Boolean existsOverlaping(LocalDateTime startDate1, LocalDateTime endDate1, Long deviceId);

    @Query(value = "select acs" +
            " from AirConditionSchedule acs" +
            " where acs.startTime <= CURRENT_TIMESTAMP" +
            " and acs.activated = false " +
            " and acs.override = false ")
    List<AirConditionSchedule> getSchedulesToActivated();

    @Query(value = "select acs from AirConditionSchedule acs" +
            " where acs.id = :deviceId" +
            " and acs.activated = true" +
            " and acs.override = false" +
            " and ( CURRENT_TIMESTAMP BETWEEN acs.startTime AND acs.endTime )")
    List<AirConditionSchedule> getCurrentSchedule(Long deviceId);

}
