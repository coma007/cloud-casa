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