package com.casa.app.util.email;

import com.casa.app.device.home.air_conditioning.schedule.AirConditionSchedule;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JSONUtil {
    public static <T> String getJsonWithDate(T obj) throws JsonProcessingException {
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());
        ow.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return ow.writeValueAsString(obj);
    }

    public static AirConditionSchedule readAirCon(String json) throws JsonProcessingException {
        ObjectMapper ow = new ObjectMapper();
        ow.registerModule(new JavaTimeModule());
        ow.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return ow.readValue(json, AirConditionSchedule.class);
    }
}
