import React from 'react'
import Menu from '../../../components/navigation/Menu/Menu'
import DeviceDetailsCSS from './DeviceDetails.module.scss'
import PageTitle from '../../../components/view/PageTitle/PageTitle'
import DeviceInfo from './info/DeviceInfo'
import DeviceManager from './manager/DeviceManager'
import Filter from './inspect/filter/Filter'
import Graph from './inspect/graph/Graph'
import WashingMachineStep from '../register/WashingMachineStep'

const DeviceDetails = () => {
    const exampleDevice = {
        Name: 'Example Device',
        RealEstateName: 'Example Estate',
        PowerSupplyType: 'Example Power Supply',
        EnergyConsumption: 'Example Energy Consumption',
    };

    const exampleAirConditioning = {
        ...exampleDevice,
        MinTemperature: 'Example Min Temperature',
        MaxTemperature: 'Example Max Temperature',
        SupportedModes: ['Cool', 'Heat', 'Dry', 'Fan'],
        deviceType: 'AirConditioning',
    };


    const exampleWashingMachine = {
        ...exampleDevice,
        SupportedModes: ['Mode1', 'Mode2', 'Mode3'],
        deviceType: 'WashingMachine',
    };

    const exampleElectricVehicleCharger = {
        ...exampleDevice,
        ChargePower: 'Example Charge Power',
        NumOfSlots: 2,
        deviceType: 'ElectricVehicleCharger',
    };

    const exampleHouseBattery = {
        ...exampleDevice,
        Size: 'Example Size',
        deviceType: 'HouseBattery',
    };

    const exampleSolarPanelSystem = {
        ...exampleDevice,
        Size: 'Example Size',
        Efficiency: 'Example Efficiency',
        deviceType: 'SolarPanelSystem',
    };

    const exampleVehicleGate = {
        ...exampleDevice,
        AllowedVehicles: ['ModeA', 'ModeB'],
        deviceType: 'VehicleGate',
    };

    let dev = exampleWashingMachine;

    return (
        <div>
            <Menu admin={false} />
            <PageTitle title="Device details" description="Manage and inspect your device." />

            <div className={DeviceDetailsCSS.content}>
                <div>
                    <DeviceInfo deviceType={dev.deviceType} device={dev}></DeviceInfo>
                    <br></br>
                    <DeviceManager deviceType={dev.deviceType} device={dev}></DeviceManager>
                </div>
                <div>
                    <Filter></Filter>
                    <Graph></Graph>
                </div>
            </div>
        </div>
    )
}

export default DeviceDetails