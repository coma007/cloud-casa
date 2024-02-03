import React, { useEffect, useRef, useState } from 'react';
import Card from '../../../../components/view/Card/Card';
import Button from '../../../../components/forms/Button/Button';
import DeviceManagerCSS from './DeviceManager.module.scss'
import InputField from '../../../../components/forms/InputField/InputField';
import { DeviceService } from '../../DeviceService';
import { Modal } from 'react-bootstrap';
import ModalWindow from '../../../../components/view/Modal/ModalWindow';
import CreateScheduleModalAirConditioning from './createSchedule/CreateScheduleModalAirConditioning';
import CreateScheduleModalSprinklerSystem from './createSchedule/CreateScheduleModalSprinklerSystem';
import CreateScheduleModalWashingMachine from './createSchedule/CreateScheduleModalWashingMachine';

const DeviceManager = (props: { deviceType: string; device: any }) => {
    const [toggleIsOn, setToggleIsOn] = useState(props.device.Status == 'ONLINE');
    const [toggleIsOpen, setToggleIsOpen] = useState(false);
    const [toggleIsPrivate, setToggleIsPrivate] = useState(false);

    const [showCreateSchedule, setShowCreateSchedule] = useState(false);

    const [temperature, setTemperature] = useState<number>(0.0);
    const [mode, setMode] = useState<string>("");
    
    const [startChargingSlot, setStartChargingSlot] = useState<number>(1);
    const [endChargingSlot, setEndChargingSlot] = useState<number>(1);
    const [maxPercentageSlot, setMaxPercentageSlot] = useState<number>(1);
    const [percentage, setPercentage] = useState<string>("");

    useEffect(() => {
        setToggleIsOn(props.device.Status === 'ONLINE');
    }, [props.device.Status]);

    useEffect(() => {
        console.log(percentage)
    }, [percentage]);

    const handleStartChargingSlot = (e) => {
        setStartChargingSlot(e.target.value);
    }

    const handleEndChargingSlot = (e) => {
        setEndChargingSlot(e.target.value);
    }

    const handleMaxPercentageSlot = (e) => {
        setMaxPercentageSlot(e.target.value);
    }

    const handleMaxPercentage = (e) => {
        setPercentage(e.target.value);
    }

    const handleStartCharging = async () => {
        await DeviceService.startCharging(props.device.Id, startChargingSlot);
    }
    
    const handleEndCharging = async () => {
        await DeviceService.endCharging(props.device.Id, endChargingSlot);
    }
    
    const handleSetMaxPercentage = async () => {
        let percentageNumber = parseFloat(percentage);
        if (!Number.isNaN(percentageNumber) && percentageNumber >= 0 && percentageNumber <= 100) {
            await DeviceService.setMaxPercentage(props.device.Id, maxPercentageSlot, percentageNumber);
        } else {
            alert("Fail")
        }
    }


    const handleIsOnClick = async () => {
        if (props.deviceType === "solar_panel_system") {
            (async function () {
                try {
                    await DeviceService.toggleSolarPanelSystem(props.device.Id);
                    // const fetchedDevices = [{} as RealEstate]
                } catch (error) {
                    console.error(error);
                }
            })()
        } else if (props.deviceType == "lamp_brightness") {
            await DeviceService.lampManager(props.device.Id);
        }
        setToggleIsOn(!toggleIsOn);

    };

    const handleIsOpenClick = async () => {
        if (props.deviceType == "vehicle_gate") {
            await DeviceService.gateManager(props.device.Id, "open");
        }
        setToggleIsOpen(!toggleIsOpen);

    };

    const handleModeChange = (e) => {
        setMode(e.target.value);
    };

    const handleOnOffCommand = () => {
        handleIsOnClick();
        try {
            if(props.deviceType == "air_conditioning")
                DeviceService.sendWorkingCommand({ working: !toggleIsOn, id: props.device.Id });
            else if(props.deviceType == "washing_machine")
                DeviceService.sendWashingMachineWorkingCommand({ working: !toggleIsOn, id: props.device.Id });
            else
                console.log("INVALID DEVICE TYPE")
        } catch (error) {
            console.error(error);
        }
    };

    const handleTemperatureChange = (e) => {
        if (isNaN(+e.target.value))
            return;
        setTemperature(Number(e.target.value));
    };

    const handleSetTemperature = () => {
        try {
            DeviceService.sendTemperatureCommand({ temperature: temperature!, id: props.device.Id });
        } catch (error) {
            console.error(error);
        }

    };

    const handleSetMode = () => {
        try {
            if(props.deviceType == "air_conditioning")
            DeviceService.sendModeCommand({ mode: mode, id: props.device.Id });
        else if(props.deviceType == "washing_machine")
        DeviceService.sendMWashingMachineModeCommand({ mode: mode, id: props.device.Id });
        else
            console.log("INVALID DEVICE TYPE")
         
        } catch (error) {
            console.error(error);
        }

    };

    const showModal = () => {
        setShowCreateSchedule(true);

    };


    const handleIsPrivateClick = () => {
        if (props.deviceType == "vehicle_gate") {
            DeviceService.gateManager(props.device.Id, "mode");
        }
        setToggleIsPrivate(!toggleIsPrivate);
    };

    useEffect(() => {
        (async function () {
            try {
                if (mode === "")
                    setMode(props.device.SupportedModes[0])
            } catch (error) {
                console.error(error);
            }
        })()
    }, [props.device.SupportedModes]);

    const renderDeviceSpecificUI = () => {

        switch (props.deviceType) {
            case 'air_conditioning':
                return (
                    <div>
                        <CreateScheduleModalAirConditioning
                            show={showCreateSchedule}
                            setShow={setShowCreateSchedule}
                            device={props.device} />
                        <div className={DeviceManagerCSS.row}>
                            <Button text={toggleIsOn ? 'Turn OFF' : 'Turn ON'} onClick={handleOnOffCommand} submit={undefined} />
                            <Button text="Custom Mode" onClick={showModal} submit={undefined} />
                        </div>
                        <div className={DeviceManagerCSS.row}>
                            <div>
                                Select Mode:
                                <select className={DeviceManagerCSS.customSelect} onChange={handleModeChange} value={mode}>
                                    {props.device.SupportedModes.map((modeS, index) => (
                                        <option key={index} value={modeS}>{modeS}</option>
                                    ))}
                                </select>
                            </div>
                            <Button text="Set" onClick={handleSetMode} submit={undefined} />
                        </div>
                        <div className={DeviceManagerCSS.row}>
                            <div className={DeviceManagerCSS.row}>
                                Set Temperature:
                                <div className={DeviceManagerCSS.input}>
                                    <InputField usage={'Temperature'} className={''} onChange={handleTemperatureChange} />
                                </div>
                            </div>
                            <div>
                                <Button text="Set" onClick={handleSetTemperature} submit={undefined} />
                            </div>
                        </div>
                    </div>
                );

            case 'washing_machine':
                return (
                    <div>
                        <CreateScheduleModalWashingMachine
                            show={showCreateSchedule}
                            setShow={setShowCreateSchedule}
                            device={props.device} />
                        <div className={DeviceManagerCSS.row}>
                            <Button text={toggleIsOn ? 'Turn OFF' : 'Turn ON'} onClick={handleOnOffCommand} submit={undefined} />
                            <Button text="Custom Mode" onClick={showModal} submit={undefined} />
                        </div>
                        <div className={DeviceManagerCSS.row}>
                            <div>
                                Select Mode:
                                <select className={DeviceManagerCSS.customSelect}
                                 onChange={handleModeChange} value={mode}>
                                    {props.device.SupportedModes.map((modeS, index) => (
                                        <option key={index} value={modeS}>{modeS}</option>
                                    ))}
                                </select>
                            </div>
                            <Button text="Set" onClick={handleSetMode} submit={undefined} />
                        </div>
                        {/* <div className={DeviceManagerCSS.row}>
                            <div></div>
                            <Button text="Schedule Washing" onClick={undefined} submit={undefined} />
                        </div> */}
                    </div>
                );

            case 'solar_panel_system':
                return (
                    <div>
                        <Button text={toggleIsOn ? 'Turn OFF' : 'Turn ON'} onClick={handleIsOnClick} submit={undefined} />
                    </div>
                );

            case 'vehicle_gate':
                return (
                    <div className={DeviceManagerCSS.row}>
                        <Button text={toggleIsOpen ? 'CLOSE' : 'OPEN'} onClick={handleIsOpenClick} submit={undefined} />
                        <Button text={toggleIsPrivate ? 'Set to PUBLIC' : 'Set to PRIVATE'} onClick={handleIsPrivateClick} submit={undefined} />
                    </div>
                );

            case 'sprinkler_system':
                return (
                    <div>
                        <CreateScheduleModalSprinklerSystem
                            show={showCreateSchedule}
                            setShow={setShowCreateSchedule}
                            device={props.device} />
                        <div className={DeviceManagerCSS.row}>
                            <Button text={toggleIsOn ? 'Turn OFF' : 'Turn ON'} onClick={handleIsOnClick} submit={undefined} />
                            <Button text="Custom Mode" onClick={showModal} submit={undefined} />
                        </div>
                    </div>
                );

            case 'lamp_brightness':
                return (
                    <div>
                        <p>
                            <Button text={toggleIsOn ? 'Turn OFF' : 'Turn ON'} onClick={handleIsOnClick} submit={undefined} />
                        </p>
                    </div>
                );

            case 'electric_vehicle_charger':
                return (
                    <div>
                        <div className={DeviceManagerCSS.row}>
                            <div>
                                Select slot:
                                <select className={DeviceManagerCSS.customSelect} onChange={handleStartChargingSlot} value={startChargingSlot}>
                                    {Array.from({ length: props.device.NumOfSlots }, (_, index) => index + 1).map((modeS, index) => (
                                        <option key={index} value={modeS}>{modeS}</option>
                                    ))}
                                </select>
                            </div>
                            <Button text="Start charging" onClick={handleStartCharging} submit={undefined} />
                        </div>
                        <div className={DeviceManagerCSS.row}>
                            <div>
                                Select slot:
                                <select className={DeviceManagerCSS.customSelect} onChange={handleEndChargingSlot} value={endChargingSlot}>
                                    {Array.from({ length: props.device.NumOfSlots }, (_, index) => index + 1).map((modeS, index) => (
                                        <option key={index} value={modeS}>{modeS}</option>
                                    ))}
                                </select>
                            </div>
                            <Button text="End charging" onClick={handleEndCharging} submit={undefined} />
                        </div>
                        <div className={DeviceManagerCSS.row}>
                            <div className={DeviceManagerCSS.row}>
                                    Select slot:
                                    <select className={DeviceManagerCSS.customSelect} onChange={handleMaxPercentageSlot} value={maxPercentageSlot}>
                                        {Array.from({ length: props.device.NumOfSlots }, (_, index) => index + 1).map((modeS, index) => (
                                            <option key={index} value={modeS}>{modeS}</option>
                                        ))}
                                    </select>
                                    <div className={DeviceManagerCSS.input}>
                                        <InputField usage={'Max Percentage'} className={''} onChange={handleMaxPercentage} />
                                    </div>
                            </div>
                            <div>
                                <Button text="Set max percentage" onClick={handleSetMaxPercentage} submit={undefined} />
                            </div>
                        </div>
                    </div>
                );

            default:
                return null;
        }
    };

    return (
        <Card>
            <h5><b>Device Manager</b></h5> <br />
            {renderDeviceSpecificUI()}
        </Card>
    );
};

export default DeviceManager;
