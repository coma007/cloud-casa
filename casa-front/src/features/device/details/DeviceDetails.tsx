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
import DetailsTable from './inspect/table/DetailsTable'
import Pagination from '../../../components/tables/Pagination/Pagination'
import { WebSocketService } from '../../../api/websocket/WebSocketService'

const DeviceDetails = () => {
    const [isFilterVisible, setFilterVisible] = useState(false);
    const [filterText, setFilterText] = useState("Show filters");
    const [deviceId, setDeviceId] = useState(-1);
    const [deviceType, setDeviceType] = useState("");
    const [dev, setDev] = useState<any>({});
    const [measurements, setMeasurements] = useState<any>({});
    const [currentPage, setCurrentPage] = useState(1);
    const [numberOfPages, setNumberOfPages] = useState(1);
    const [socket, setSocket] = useState(null);
    const location = useLocation();

    useEffect(() => {
        const receivedProps: { id: number, type: string } = location.state;
        // console.log(receivedProps);
        setDeviceType(receivedProps.type);
        setDeviceId(receivedProps.id);
    }, [location.state]);

    useEffect(() => {
        (async function () {
            try {
                if (deviceId > 0) {
                    const fetchedDevice = await DeviceService.getDeviceDetails(deviceId);
                    // console.log(fetchedDevice)
                    populateData(fetchedDevice)
                }
            } catch (error) {
                console.error(error);
            }
        })()
    }, [deviceId]);

    const createWebSocket = (fetchedDevice: any, measurements: any) => {

        console.log(fetchedDevice.type)
        if (fetchedDevice.type === "house_battery") {
            WebSocketService.createSocket("/topic/house-battery-power-usage/" + deviceId, (message: { topic: string, message: string, fromId: string, toId: string, attachment: any }) => {
                let newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, power: message.attachment.power }, ...measurements.measurements]
                if (newMeasurements.length > 10) {
                    newMeasurements = newMeasurements.slice(0, 10)
                }
                setMeasurements({
                    deviceType: measurements.deviceType,
                    deviceId: measurements.deviceId,
                    from: measurements.from,
                    to: measurements.to,
                    measurements: newMeasurements,
                })

            })
        }
        else if (fetchedDevice.type === "lamp_brightness") {
            WebSocketService.createSocket("/topic/lamp_brightness/" + deviceId, (message: { topic: string, message: string, fromId: string, toId: string, attachment: any }) => {
                // console.log(measurements)
                if (currentPage == 1) {
                    let newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, brightness: message.attachment.brightness }, ...measurements.measurements]
                    if (newMeasurements.length > 10) {
                        newMeasurements = newMeasurements.slice(0, 10)
                    }
                    setMeasurements({
                        deviceType: measurements.deviceType,
                        deviceId: measurements.deviceId,
                        from: measurements.from,
                        to: measurements.to,
                        measurements: newMeasurements,
                    })
                }
            })
        }

        else if (fetchedDevice.type === "vehicle_gate") {
            WebSocketService.createSocket("/topic/" + gateMode + "/" + deviceId, (message: { topic: string, message: string, fromId: string, toId: string, attachment: any }) => {
                // console.log(measurements)
                if (currentPage == 1) {
                    let newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, licence_plates: message.attachment.licence_plates }, ...measurements.measurements]
                    if (gateMode === "vehicle_gate_command") {
                        console.log(message.attachment)
                        newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, is_open: message.attachment.is_open, user: message.attachment.user }, ...measurements.measurements]
                    }
                    else if (gateMode === "vehicle_gate_mode") {
                        newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, is_private: message.attachment.is_private, user: message.attachment.user }, ...measurements.measurements]
                    }
                    if (newMeasurements.length > 10) {
                        newMeasurements = newMeasurements.slice(0, 10)
                    }
                    setMeasurements({
                        deviceType: measurements.deviceType,
                        deviceId: measurements.deviceId,
                        from: measurements.from,
                        to: measurements.to,
                        measurements: newMeasurements,
                    })
                }
            })
        }
        else if (fetchedDevice.type === "ambient_sensor") {
            WebSocketService.createSocket("/topic/ambient_sensor_reading/"+deviceId, (message: {topic : string, message : string, fromId : string, toId : string, attachment : any}) => {
                let newMeasurements = [{id : message.attachment.id, timestamp : (new Date(message.attachment.timestamp)).getTime() / 1000, temperature : message.attachment.temperature, humidity: message.attachment.humidity}, ...measurements.measurements]
                if (newMeasurements.length > 10) {
                   newMeasurements = newMeasurements.slice(0, 10)
                }
                setMeasurements({
                    deviceType : measurements.deviceType,
                    deviceId : measurements.deviceId,
                    from : measurements.from,
                    to : measurements.to,
                    measurements : newMeasurements,
                })
                
            })
        }

        else if (fetchedDevice.type === "air_conditioning") {
            WebSocketService.createSocket("/topic/air_conditioning_commands/"+deviceId, (message: {topic : string, message : string, fromId : string, toId : string, attachment : any}) => {
                let newMeasurements = [{id : message.attachment.id, timestamp : (new Date(message.attachment.timestamp)).getTime() / 1000, attachment : message.attachment}, ...measurements.measurements]
                if (newMeasurements.length > 10) {
                   newMeasurements = newMeasurements.slice(0, 10)
                }
                setMeasurements({
                    deviceType : measurements.deviceType,
                    deviceId : measurements.deviceId,
                    from : measurements.from,
                    to : measurements.to,
                    measurements : newMeasurements,
                })
                
            })
        }
    }

    useEffect(() => {
        if (dev.type === "house_battery") {
            WebSocketService.unsubscribe()
            WebSocketService.openSocket("/topic/house-battery-power-usage/" + deviceId, (message: { topic: string, message: string, fromId: string, toId: string, attachment: any }) => {
                // console.log(measurements)
                if (currentPage == 1) {
                    let newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, power: message.attachment.power }, ...measurements.measurements]
                    if (newMeasurements.length > 10) {
                        newMeasurements = newMeasurements.slice(0, 10)
                    }
                    setMeasurements({
                        deviceType: measurements.deviceType,
                        deviceId: measurements.deviceId,
                        from: measurements.from,
                        to: measurements.to,
                        measurements: newMeasurements,
                    })
                }
            })
        }
        else if (dev.type === "lamp_brightness") {
            WebSocketService.unsubscribe()
            WebSocketService.openSocket("/topic/lamp_brightness/" + deviceId, (message: { topic: string, message: string, fromId: string, toId: string, attachment: any }) => {
                // console.log(measurements)
                if (currentPage == 1) {
                    let newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, brightness: message.attachment.brightness }, ...measurements.measurements]
                    if (newMeasurements.length > 10) {
                        newMeasurements = newMeasurements.slice(0, 10)
                    }
                    setMeasurements({
                        deviceType: measurements.deviceType,
                        deviceId: measurements.deviceId,
                        from: measurements.from,
                        to: measurements.to,
                        measurements: newMeasurements,
                    })
                }
            })
        }
        else if (dev.type === "vehicle_gate") {
            WebSocketService.unsubscribe()
            WebSocketService.openSocket("/topic/" + gateMode + "/" + deviceId, (message: { topic: string, message: string, fromId: string, toId: string, attachment: any }) => {
                // console.log(measurements)
                if (currentPage == 1) {
                    let newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, licence_plates: message.attachment.licence_plates }, ...measurements.measurements]
                    if (gateMode === "vehicle_gate_command") {
                        newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, is_open: message.attachment.is_open, user: message.attachment.user }, ...measurements.measurements]
                    }
                    else if (gateMode === "vehicle_gate_mode") {
                        console.log(message.attachment)
                        newMeasurements = [{ id: message.attachment.id, timestamp: (new Date(message.attachment.timestamp)).getTime() / 1000, is_private: message.attachment.is_private, user: message.attachment.user }, ...measurements.measurements]
                    }
                    if (newMeasurements.length > 10) {
                        newMeasurements = newMeasurements.slice(0, 10)
                    }
                    setMeasurements({
                        deviceType: measurements.deviceType,
                        deviceId: measurements.deviceId,
                        from: measurements.from,
                        to: measurements.to,
                        measurements: newMeasurements,
                    })
                }
            })
        }
        else if (dev.type === "ambient_sensor") {    
            WebSocketService.unsubscribe()
            WebSocketService.openSocket("/topic/ambient_sensor_reading/"+deviceId, (message: {topic : string, message : string, fromId : string, toId : string, attachment : any}) => {
                console.log(measurements)
                if (currentPage == 1) {
                    let newMeasurements = [{id : message.attachment.id, timestamp : (new Date(message.attachment.timestamp)).getTime() / 1000, temperature : message.attachment.temperature, humidity : message.attachment.humidity}, ...measurements.measurements]
                    if (newMeasurements.length > 10) {
                       newMeasurements = newMeasurements.slice(0, 10)
                    }
                    setMeasurements({
                        deviceType : measurements.deviceType,
                        deviceId : measurements.deviceId,
                        from : measurements.from,
                        to : measurements.to,
                        measurements : newMeasurements,
                    })
                }
            })
        }

        else if (dev.type === "air_conditioning") {    
            WebSocketService.unsubscribe()
            WebSocketService.openSocket("/topic/air_conditioning_commands/"+deviceId, (message: {topic : string, message : string, fromId : string, toId : string, attachment : any}) => {
                console.log(measurements)
                if (currentPage == 1) {
                    let newMeasurements = [{id : message.attachment.id, timestamp : (new Date(message.attachment.timestamp)).getTime() / 1000, attachment : message.attachment}, ...measurements.measurements]
                    if (newMeasurements.length > 10) {
                       newMeasurements = newMeasurements.slice(0, 10)
                    }
                    setMeasurements({
                        deviceType : measurements.deviceType,
                        deviceId : measurements.deviceId,
                        from : measurements.from,
                        to : measurements.to,
                        measurements : newMeasurements,
                    })
                }
            })
        }
    }, [measurements, currentPage])


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
            Id: deviceId,
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
                    measurementTopic: 'air_conditioning',
                })
                break;
            case "washing_machine":
                setDev({
                    ...baseDevice,
                    SupportedModes: device.supportedModes,
                    type: 'washing_machine',
                    measurementTopic: 'washing_machine',
                })
                break;
            case "electric_vehicle_charger":
                setDev({
                    ...baseDevice,
                    ChargePower: device.chargePower.toString() + " kWh",
                    NumOfSlots: device.numOfSlots,
                    type: 'electric_vehicle_charger',
                    measurementTopic: 'electric_vehicle_charger',
                })
                break;
            case "house_battery":
                setDev({
                    ...baseDevice,
                    Size: device.size.toString() + ' mAH',
                    type: 'house_battery',
                    measurementTopic: 'house_battery_power_usage',
                    measurementLabel: 'Power usage',
                })
                break;
            case "solar_panel_system":
                setDev({
                    ...baseDevice,
                    Size: device.size.toString(),
                    Efficiency: device.efficiency.toString() + ' %',
                    type: 'solar_panel_system',
                    measurementTopic: 'solar_panel_system_command',
                })
                break;
            case "vehicle_gate":
                setDev({
                    ...baseDevice,
                    AllowedVehicles: device.allowedVehicles,
                    type: 'vehicle_gate',
                    measurementTopic: 'vehicle_gate_licence_plates',
                })
                break;
            case "ambient_sensor":
                setDev({
                    ...baseDevice,
                    type: 'ambient_sensor',
                    measurementTopic: 'ambient_sensor',
                })
                break;
            case "lamp":
                setDev({
                    ...baseDevice,
                    type: 'lamp_brightness',
                    measurementTopic: 'lamp_brightness',
                    measurementLabel: 'Lamp brightness',
                })
                break;
            case "sprinkler_system":
                setDev({
                    ...baseDevice,
                    type: 'sprinkler_system',
                    measurementTopic: 'sprinkler_system',
                })
                break;
            default:
                break;
        }
    }

    let [username, setUsername] = useState('');

    const handleUsernameFilterClick = () => {
        console.log(username);
        setCurrentPage(1);
        (async () => {
            const fetchedNumberOfPages = await DeviceService.getPageNumber(deviceId, dev.measurementTopic, fromDate, toDate, username);
            setNumberOfPages(fetchedNumberOfPages);
        })();
        (async () => {
            const fetchedMeasuremenets = await DeviceService.filter(dev.Id, dev.measurementTopic, fromDate, toDate, username, 1);
            setMeasurements(fetchedMeasuremenets)
        })();
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

    const handleFromTimeChange = (e) => {
        const selectedFromDate = e.target.value;
        setFromDate(selectedFromDate);

        const nextMonth = new Date(selectedFromDate);
        nextMonth.setMonth(nextMonth.getMonth() + 1);
        setToDateMin(nextMonth.toISOString().split('T')[0]);
        setToDate(nextMonth.toISOString().split('T')[0]);
    };
    const handleDateFilterClick = (from: string, to: string) => {
        if(["ambient_sensor"].includes(dev.type)){
            let fromTime = new Date(from);
            let toTime = new Date(to); 
            let diff = (toTime.getTime() - fromTime.getTime()) / 1000 / 60 / 60 / 24;
            if(diff > 30){
                console.log('Too big diffrence');
                return;
            }
        }
        setFromDate(from);
        setToDate(to);
        setCurrentPage(1);
        (async () => {
            const fetchedNumberOfPages = await DeviceService.getPageNumber(dev.Id, dev.measurementTopic, new Date(from).toISOString(), new Date(to).toISOString(), username);
            setNumberOfPages(fetchedNumberOfPages);
        })();
        (async () => {
            const fetchedMeasurements = await DeviceService.filter(dev.Id, dev.measurementTopic, new Date(from).toISOString(), new Date(to).toISOString(), username, 1);
            setMeasurements(fetchedMeasurements);
        })();

    };
    // useEffect(()=> {
    //     (async () => {
    //         const fetchedMeasurements = await DeviceService.filter(dev.Id, dev.measurementTopic, new Date(fromDate).toISOString(), new Date(toDate).toISOString(), username, 1);
    //         setMeasurements(fetchedMeasurements);
    //     })();
    // }, [numberOfPages])

    useEffect(() => {
        (async () => {
            if (Object.keys(dev).length > 0) {
                const fetchedNumberOfPages = await DeviceService.getPageNumber(dev.Id, dev.measurementTopic, fromDate, toDate, username);
                setNumberOfPages(fetchedNumberOfPages);
            }
        })();
        (async () => {
            if (Object.keys(dev).length > 0) {
                const fetchedMeasurements = await DeviceService.filter(deviceId, dev.measurementTopic, fromDate, toDate, username, 1);
                // console.log(fetchedMeasurements);
                setMeasurements(fetchedMeasurements);
                createWebSocket(dev, fetchedMeasurements);
            }
        })()
    }, [dev, dev.measurementTopic])

    const resetFilters = () => {

        if (fromDate == "" || toDate == "") {
            return;
        }
        setFromDate('');
        setToDate('');
        setUsername('');
        (async () => {
            if (Object.keys(dev).length > 0) {
                const fetchedNumberOfPages = await DeviceService.getPageNumber(dev.Id, dev.measurementTopic, new Date(fromDate).toISOString(), new Date(toDate).toISOString(), username);
                setNumberOfPages(fetchedNumberOfPages);
            }
        })();
        (async () => {
            if (Object.keys(dev).length > 0) {
                const fetchedNumberOfPages = await DeviceService.getPageNumber(dev.Id, dev.measurementTopic, new Date(fromDate).toISOString(), new Date(toDate).toISOString(), username);
                setNumberOfPages(fetchedNumberOfPages);
                const fetchedMeasurements = await DeviceService.filter(deviceId, dev.measurementTopic, '', '', '', 1);
                setMeasurements(fetchedMeasurements)
            }
        })()
    }

    const changePage = (pageNumber: number) => {
        setCurrentPage(pageNumber)
    }

    useEffect(() => {
        (async () => {
            if (Object.keys(dev).length > 0) {
                const fetchedMeasurements = await DeviceService.filter(deviceId, dev.measurementTopic, fromDate, toDate, username, currentPage);
                setMeasurements(fetchedMeasurements)
            }
        })()
    }, [currentPage])

    const [ambientMeasurement, setAmbientMeasurement] = useState("temperature");


    const [gateMode, setGateMode] = useState("vehicle_gate_licence_plates")

    const handleGateModeChange = (mode: string) => {
        setGateMode(mode);
        dev.measurementTopic = mode;
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
                    {   
                        (["ambient_sensor"].includes(dev.type)) &&
                        (
                        <>
                            <div className={DeviceDetailsCSS.buttons} >
                                <Button className={DeviceDetailsCSS.ambientButton} text={"Temperature"} onClick={() => setAmbientMeasurement("temperature")} submit={undefined}></Button>
                                 <Button className={DeviceDetailsCSS.ambientButton} text={"Humidity"} onClick={() => setAmbientMeasurement("humidity")} submit={undefined}></Button>
                             </div>
                        </>
                        )
                        
                    }
                    {deviceType == "vehicle_gate" &&
                        <div>
                            <br></br>
                            <small>Chose a mode of querying data on the right</small> <br></br>
                            <button onClick={() => { handleGateModeChange("vehicle_gate_licence_plates") }} className={DeviceDetailsCSS.smallButton}>license plates</button>
                            <button onClick={() => handleGateModeChange("vehicle_gate_command")} className={DeviceDetailsCSS.smallButton}>open/close</button>
                            <button onClick={() => handleGateModeChange("vehicle_gate_mode")} className={DeviceDetailsCSS.smallButton}>public/private</button>
                        </div>}
                </div>
                <div>
                    <div className={DeviceDetailsCSS.row}>
                        <Button text={filterText} onClick={handleFilterToggle} submit={undefined}></Button>
                        <Button text={"Reset filters"} onClick={resetFilters} submit={undefined} ></Button>
                    </div>
                    {isFilterVisible && (<div>
                        {(["ambient_sensor"].includes(dev.type) && 
                                      <>
                                      <input
                                      type="datetime-local"
                                      id="scheduleStart"
                                      name="from"
                                      value={fromDate}
                                      onChange={handleFromTimeChange}
                                      className={DeviceDetailsCSS.filterings} />
                                      <input
                                      type="datetime-local"
                                      id="scheduleEnd"
                                      name="from"
                                      value={toDate}
                                      onChange={(e) => setToDate(e.target.value)}
                                      className={DeviceDetailsCSS.filterings} />
                                      <Button text={'Filter'}  onClick={() =>handleDateFilterClick(fromDate, toDate)}  submit={undefined} className={DeviceDetailsCSS.filterings} />
                                      </>
                        ) ||
                        <>
                            <hr></hr>
                            <FilterDate fromDate={fromDate} toDate={toDate} handleSubmit={handleDateFilterClick} handleFromDateChange={handleFromDateChange} toDateMin={toDateMin} setToDate={setToDate}></FilterDate>
                            <hr></hr>
                        </>
                        }
                        {
                            (!["ambient_sensor", "lamp"].includes(dev.type)) &&
                            <>
                                <FilterUser username={username} onInputChange={setUsername} handleSubmit={handleUsernameFilterClick}></FilterUser>
                                <hr></hr>
                            </>
                        }
                    </div>)}
                    {
                        // (["solar_panel_system"].includes(dev.type)) &&
                        (["solar_panel_system", "vehicle_gate", "air_conditioning"].includes(dev.type)) &&
                        (
                            <>
                                <DetailsTable measurements={measurements} deviceType={deviceType} topic={gateMode} />
                                <div>
                                    <Pagination currentPage={currentPage} numberOfPages={numberOfPages} onClick={changePage} />
                                </div>
                            </>)
                    }
                    {   
                        (["house_battery", "lamp", "ambient_sensor"].includes(dev.type)) &&
                        (
                        <>
                            <Graph deviceType={deviceType} measurements={measurements} label={dev.measurementLabel} ambientMeasurement={ambientMeasurement} />
                            <div>
                                <Pagination currentPage={currentPage} numberOfPages={numberOfPages} onClick={changePage} />
                            </div>
                        </>
                        )

                    }
              
                </div>
            </div>
        </div>
    )
}

export default DeviceDetails