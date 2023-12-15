import React, { useState } from 'react'
import Menu from '../../../components/navigation/Menu/Menu'
import DeviceDetailsCSS from './DeviceDetails.module.scss'
import PageTitle from '../../../components/view/PageTitle/PageTitle'
import DeviceInfo from './info/DeviceInfo'
import DeviceManager from './manager/DeviceManager'
import Graph from './inspect/graph/Graph'
import FilterUser from './inspect/filter/FilterUser'
import FilterDate from './inspect/filter/FilterDate'
import Button from '../../../components/forms/Button/Button'

const DeviceDetails = () => {
    const [isFilterVisible, setFilterVisible] = useState(false);

    const handleFilterToggle = () => {
        setFilterVisible(!isFilterVisible);
    };


    const exampleDevice = {
        Name: 'Example Device',
        RealEstateName: 'Example Estate',
        PowerSupplyType: 'Example Power Supply',
        EnergyConsumption: 'Example Energy Consumption',
    };

    const exampleAirConditioning = {
        ...exampleDevice,
        MinTemperature: 30,
        MaxTemperature: 95,
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

    const exampleAmbientSensor = {
        ...exampleDevice,
        deviceType: 'AmbientSensor',
    };

    const exampleLamp = {
        ...exampleDevice,
        deviceType: 'Lamp',
    };

    const exampleSprinklerSystem = {
        ...exampleDevice,
        deviceType: 'SprinklerSystem',
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
                    {
                        (!["AmbientSensor", "HouseBattery", "ElectricVehicleCharger"].includes(dev.deviceType)) &&
                        <DeviceManager deviceType={dev.deviceType} device={dev}></DeviceManager>
                    }
                </div>
                <div>
                    <div className={DeviceDetailsCSS.row}>
                        <Button text={"Show filters"} onClick={handleFilterToggle} submit={undefined}></Button>
                        <Button text={"Reset filters"} onClick={undefined} submit={undefined} ></Button>
                    </div>
                    {isFilterVisible && (<div>
                        <hr></hr>
                        <FilterDate></FilterDate>
                        <hr></hr>
                        {
                            (!["AmbientSensor", "Lamp"].includes(dev.deviceType)) &&
                            <>
                                <FilterUser></FilterUser>
                                <hr></hr>
                            </>
                        }
                    </div>)}
                    <Graph></Graph>
                </div>
            </div>
        </div>
    )
}

export default DeviceDetails