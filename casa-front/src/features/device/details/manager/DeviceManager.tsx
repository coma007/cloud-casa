import React, { useEffect, useRef, useState } from 'react';
import Card from '../../../../components/view/Card/Card';
import Button from '../../../../components/forms/Button/Button';
import DeviceManagerCSS from './DeviceManager.module.scss'
import InputField from '../../../../components/forms/InputField/InputField';
import { DeviceService } from '../../DeviceService';
import { Modal } from 'react-bootstrap';
import ModalWindow from '../../../../components/view/Modal/ModalWindow';
import CreateScheduleModal from './createSchedule/CreateScheduleModal';

const DeviceManager = (props: { deviceType: string; device: any }) => {
    const [toggleIsOn, setToggleIsOn] = useState(props.device.Status);
    const [toggleIsOpen, setToggleIsOpen] = useState(false);
    const [toggleIsPrivate, setToggleIsPrivate] = useState(false);

    const [showCreateSchedule, setShowCreateSchedule] = useState(false);

    const [temperature, setTemperature] = useState<number>(0.0);
    const [mode, setMode] = useState<string>("");


    const handleIsOnClick = () => {
        setToggleIsOn(!toggleIsOn);
    };

    const handleIsOpenClick = () => {
        setToggleIsOpen(!toggleIsOpen);

    };

    const handleModeChange = (e) => {
        setMode(e.target.value);
    };

    const handleOnOffCommand = () => {
        handleIsOnClick();
        try {
            DeviceService.sendWorkingCommand({working: !toggleIsOn, id: props.device.Id});
        } catch (error) {
            console.error(error);
        }
    };

    const handleTemperatureChange = (e) => {
        setTemperature(Number(e.target.value));
    };

    const handleSetTemperature = () => {
        try {
            DeviceService.sendTemperatureCommand({temperature: temperature!, id: props.device.Id});
        } catch (error) {
            console.error(error);
        }
        
    };

    const handleSetMode = () => {
        try {
            DeviceService.sendModeCommand({mode: mode, id: props.device.Id});
        } catch (error) {
            console.error(error);
        }
    
    };

    const showModal = () => {
        setShowCreateSchedule(true);
    
    };


    const handleIsPrivateClick = () => {
        setToggleIsPrivate(!toggleIsPrivate);
    };

    useEffect(() => {
        (async function () {
            try {
                if(mode === "")
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
                        <CreateScheduleModal 
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
                        <div className={DeviceManagerCSS.row}>
                            <div>
                                Select Mode:
                                <select className={DeviceManagerCSS.customSelect}>
                                    {props.device.SupportedModes.map((mode, index) => (
                                        <option key={index}>{mode}</option>
                                    ))}
                                </select>
                            </div>
                            <Button text={toggleIsOn ? 'STOP' : 'START'} onClick={handleIsOnClick} submit={undefined} />
                        </div>
                        <div className={DeviceManagerCSS.row}>
                            <div></div>
                            <Button text="Schedule Washing" onClick={undefined} submit={undefined} />
                        </div>
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
                        <Button text={toggleIsOn ? 'CLOSE' : 'OPEN'} onClick={handleIsOpenClick} submit={undefined} />
                        <Button text={toggleIsOn ? 'Set to PUBLIC' : 'Set to PRIVATE'} onClick={handleIsPrivateClick} submit={undefined} />
                    </div>
                );

            case 'sprinkler_system':
                return (
                    <div className={DeviceManagerCSS.row}>
                        <Button text={toggleIsOn ? 'Turn OFF' : 'Turn ON'} onClick={handleIsOnClick} submit={undefined} />
                        <Button text="Custom Mode" onClick={undefined} submit={undefined} />
                    </div>
                );

            case 'lamp':
                return (
                    <div>
                        <p>
                            <Button text={toggleIsOn ? 'Turn OFF' : 'Turn ON'} onClick={handleIsOnClick} submit={undefined} />
                        </p>
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
