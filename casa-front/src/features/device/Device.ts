export interface DeviceCreate {
    type: string,
    device: any
}

export interface DeviceDetails {
    id: number,
    name: string,
    realEstateName: string
    powerSupplyType: string
    energyConsumption: number,
    type: string,
    schedule?: SprinklerSchedule
}

export interface TemperatureCommand {
    temperature: number,
    id: number
}

export interface WorkingCommand {
    working: boolean,
    id: number
}

export interface ModeCommand {
    mode: string,
    id: number
}

export interface AirConditionerSchedule {
    startTime: string;
    endTime: string;
    deviceId: number;

    working: boolean;
    mode: string | undefined;
    temperature: number | undefined;

    repeating: boolean;
    repeatingDaysIncrement: number;
}

export interface WashingMachineSchedule {
    startTime: string;
    endTime: string;
    deviceId: number;

    working: boolean;
    mode: string | undefined;
}

export interface SprinklerSchedule {
    startTime: Date;
    endTime: Date;
    scheduledDays: boolean[];
}

