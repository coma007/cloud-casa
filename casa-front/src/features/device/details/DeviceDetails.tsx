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
    const [filterText, setFilterText] = useState("Show filters");

    const handleFilterToggle = () => {
        if (isFilterVisible) {
            setFilterText("Show filters");
        }
        else {
            setFilterText("Hide filters")
        }
        setFilterVisible(!isFilterVisible);
    };


    const exampleDevice = {
        Id: 123,
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
        deviceType: 'air_conditioning',
    };


    const exampleWashingMachine = {
        ...exampleDevice,
        SupportedModes: ['Mode1', 'Mode2', 'Mode3'],
        deviceType: 'washing_machine',
    };

    const exampleElectricVehicleCharger = {
        ...exampleDevice,
        ChargePower: 'Example Charge Power',
        NumOfSlots: 2,
        deviceType: 'electric_vehicle_charger',
    };

    const exampleHouseBattery = {
        ...exampleDevice,
        Size: 'Example Size',
        deviceType: 'house_battery',
    };

    const exampleSolarPanelSystem = {
        ...exampleDevice,
        Size: 'Example Size',
        Efficiency: 'Example Efficiency',
        deviceType: 'solar_panel_system',
    };

    const exampleVehicleGate = {
        ...exampleDevice,
        AllowedVehicles: ['ModeA', 'ModeB'],
        deviceType: 'vehicle_gate',
    };

    const exampleAmbientSensor = {
        ...exampleDevice,
        deviceType: 'ambient_sensor',
    };

    const exampleLamp = {
        ...exampleDevice,
        deviceType: 'lamp',
    };

    const exampleSprinklerSystem = {
        ...exampleDevice,
        deviceType: 'sprinkler_system',
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
                        (!["ambient_sensor", "house_battery", "electric_vehicle_charger"].includes(dev.deviceType)) &&
                        <DeviceManager deviceType={dev.deviceType} device={dev}></DeviceManager>
                    }
                </div>
                <div>
                    <div className={DeviceDetailsCSS.row}>
                        <Button text={filterText} onClick={handleFilterToggle} submit={undefined}></Button>
                        <Button text={"Reset filters"} onClick={undefined} submit={undefined} ></Button>
                    </div>
                    {isFilterVisible && (<div>
                        <hr></hr>
                        <FilterDate deviceType={dev.deviceType} device={dev}></FilterDate>
                        <hr></hr>
                        {
                            (!["ambient_sensor", "lamp"].includes(dev.deviceType)) &&
                            <>
                                <FilterUser deviceType={dev.deviceType} device={dev}></FilterUser>
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