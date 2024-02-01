package com.casa.app.device.home.washing_machine.measuraments.commands;

import com.casa.app.device.measurement.AbstractMeasurement;

public abstract class WashingMachineCommand extends AbstractMeasurement {
    public abstract String toMessage();
}
