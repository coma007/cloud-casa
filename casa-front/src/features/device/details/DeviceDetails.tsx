import React, { useEffect, useState } from 'react'
import Menu from '../../../components/navigation/Menu/Menu'
import DeviceDetailsCSS from './DeviceDetails.module.scss'
import PageTitle from '../../../components/view/PageTitle/PageTitle'
import DeviceInfo from './info/DeviceInfo'
import DeviceManager from './manager/DeviceManager'
import Graph from './inspect/graph/Graph'
import FilterUser from './inspect/filter/FilterUser'
import FilterDate from './inspect/filter/FilterDate'
import Button from '../../../components/forms/Button/Button'
import { DeviceService } from '../DeviceService'
import { useLocation } from 'react-router-dom'

const DeviceDetails = () => {
    const [isFilterVisible, setFilterVisible] = useState(false);
    const [filterText, setFilterText] = useState("Show filters");
    const [deviceId, setDeviceId] = useState(-1);
    const [deviceType, setDeviceType] = useState("");
    const [dev, setDev] = useState<any>({});
    const location = useLocation();

    useEffect(() => {
        const receivedProps : {id : number, type : string} = location.state;
        console.log(receivedProps);
        setDeviceType(receivedProps.type);
        setDeviceId(receivedProps.id);
    }, [location.state]);

    useEffect(() => {
        (async function () {
            try {
                const fetchedDevice = await DeviceService.getDeviceDetails(deviceId);
                populateData(fetchedDevice)
            } catch (error) {
                console.error(error);
            }
        })()
    }, [deviceId]);

    const handleFilterToggle = () => {
        if (isFilterVisible) {
            setFilterText("Show filters");
        }
        else {
            setFilterText("Hide filters")
        }
        setFilterVisible(!isFilterVisible);
    };

    const populateData = (device) => {
        let baseDevice = {
            Id: 123,
            Name: device.name,
            RealEstateName: device.realEstateName,
            PowerSupplyType: device.powerSupplyType,
            EnergyConsumption: device.energyConsumption,
            Status: device.status,
        }
        switch (deviceType) {
            case "air_conditioning":
                setDev({
                    ...baseDevice,
                    MinTemperature: device.minTemperature,
                    MaxTemperature: device.maxTemperature,
                    SupportedModes: device.supportedModes,
                    type: 'air_conditioning',
                })
                break;
            case "washing_machine":
                setDev({
                    ...baseDevice,
                    SupportedModes: device.supportedModes,
                    type: 'washing_machine',
                })
                break;
            case "electric_vehicle_charger":
                setDev({
                    ...baseDevice,
                    ChargePower: device.chargePower.toString() + " kWh",
                    NumOfSlots: device.numOfSlots,
                    type: 'electric_vehicle_charger',
                })
                break;
            case "house_battery":
                setDev({
                    ...baseDevice,
                    Size: device.size.toString() + ' mAH',
                    type: 'house_battery',
                })
                break;
            case "solar_panel_system":
                setDev({
                    ...baseDevice,
                    Size: device.size.toString() + ' m^2',
                    Efficiency: device.efficiency.toString() + ' %',
                    type: 'solar_panel_system',
                })
                break;
            case "vehicle_gate":
                setDev({
                    ...baseDevice,
                    AllowedVehicles: device.allowedVehicles,
                    type: 'vehicle_gate',
                })
                break;
            case "ambient_sensor":
                setDev({
                    ...baseDevice,
                    type: 'ambient_sensor',
                })
                break;
            case "lamp":
                setDev({
                    ...baseDevice,
                    type: 'lamp_brightness',
                })
                break;
            case "sprinkler_system":
                setDev({
                    ...baseDevice,
                    type: 'sprinkler_system',
                })
                break;
            default:
                break;
        }
    }

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
        type: 'air_conditioning',
    };


    const exampleWashingMachine = {
        ...exampleDevice,
        SupportedModes: ['Mode1', 'Mode2', 'Mode3'],
        type: 'washing_machine',
    };

    const exampleElectricVehicleCharger = {
        ...exampleDevice,
        ChargePower: 'Example Charge Power',
        NumOfSlots: 2,
        type: 'electric_vehicle_charger',
    };

    const exampleHouseBattery = {
        ...exampleDevice,
        Size: 'Example Size',
        type: 'house_battery',
    };

    const exampleSolarPanelSystem = {
        ...exampleDevice,
        Size: 'Example Size',
        Efficiency: 'Example Efficiency',
        type: 'solar_panel_system',
    };

    const exampleVehicleGate = {
        ...exampleDevice,
        AllowedVehicles: ['ModeA', 'ModeB'],
        type: 'vehicle_gate',
    };

    const exampleAmbientSensor = {
        ...exampleDevice,
        type: 'ambient_sensor',
    };

    const exampleLamp = {
        ...exampleDevice,
        type: 'lamp_brightness',
    };

    const exampleSprinklerSystem = {
        ...exampleDevice,
        type: 'sprinkler_system',
    };

    // let dev = exampleHouseBattery;



    let [username, setUsername] = useState('');

    const handleUsernameFilterClick = () => {
        console.log(username);
        DeviceService.filter(dev.Id, dev.type, fromDate, toDate, username);
    }

    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [toDateMin, setToDateMin] = useState('');

    const handleFromDateChange = (e) => {
        const selectedFromDate = e.target.value;
        setFromDate(selectedFromDate);

        const nextDay = new Date(selectedFromDate);
        nextDay.setDate(nextDay.getDate() + 1);
        setToDateMin(nextDay.toISOString().split('T')[0]);
        setToDate(nextDay.toISOString().split('T')[0]);
    };

    const handleDateFilterClick = (from: string, to: string) => {
        setFromDate(from);
        setToDate(to);

        DeviceService.filter(dev.Id, dev.type, new Date(from).toISOString(), new Date(to).toISOString(), username);
    };

    const resetFilters = () => {
        setFromDate('');
        setToDate('');
        setUsername('');
    }



    return (
        <div>
            <Menu admin={false} />
            <PageTitle title="Device details" description="Manage and inspect your device." />

            <div className={DeviceDetailsCSS.content}>
                <div>
                    <DeviceInfo deviceType={dev.type} device={dev}></DeviceInfo>
                    <br></br>
                    {
                        (!["ambient_sensor", "house_battery", "electric_vehicle_charger"].includes(dev.type)) &&
                        <DeviceManager deviceType={dev.type} device={dev}></DeviceManager>
                    }
                </div>
                <div>
                    <div className={DeviceDetailsCSS.row}>
                        <Button text={filterText} onClick={handleFilterToggle} submit={undefined}></Button>
                        <Button text={"Reset filters"} onClick={resetFilters} submit={undefined} ></Button>
                    </div>
                    {isFilterVisible && (<div>
                        <hr></hr>
                        <FilterDate fromDate={fromDate} toDate={toDate} handleSubmit={handleDateFilterClick} handleFromDateChange={handleFromDateChange} toDateMin={toDateMin} setToDate={setToDate}></FilterDate>
                        <hr></hr>
                        {
                            (!["ambient_sensor", "lamp"].includes(dev.type)) &&
                            <>
                                <FilterUser username={username} onInputChange={setUsername} handleSubmit={handleUsernameFilterClick}></FilterUser>
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