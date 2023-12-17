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

export interface Schedule {
    startTime: string;
    endTime: string;
    deviceId: number;
    
    working: boolean;
    mode: string | undefined;
    temperature: number | undefined;
}



