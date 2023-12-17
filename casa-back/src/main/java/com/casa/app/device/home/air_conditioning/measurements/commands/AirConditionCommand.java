package com.casa.app.device.home.air_conditioning.measurements.commands;

import com.casa.app.device.measurement.AbstractMeasurement;

public abstract class AirConditionCommand extends AbstractMeasurement {
    public abstract String toMessage();
}
