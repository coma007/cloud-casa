import React, { useEffect, useState } from 'react';
import Card from '../../../../components/view/Card/Card';
import Button from '../../../../components/forms/Button/Button';
import DeviceManagerCSS from './DeviceManager.module.scss'
import InputField from '../../../../components/forms/InputField/InputField';
import { DeviceService } from '../../DeviceService';

const DeviceManager = (props: { deviceType: string; device: any }) => {
    const [toggleIsOn, setToggleIsOn] = useState(props.device.Status == 'ONLINE');
    const [toggleIsOpen, setToggleIsOpen] = useState(false);
    const [toggleIsPrivate, setToggleIsPrivate] = useState(false);

    useEffect(() => {
        setToggleIsOn(props.device.Status === 'ONLINE');
    }, [props.device.Status]);

    const handleIsOnClick = () => {
        if (props.deviceType === "solar_panel_system") {
            (async function () {
                try {
                    await DeviceService.toggleSolarPanelSystem(props.device.Id);
                    setToggleIsOn(!toggleIsOn);
                    // const fetchedDevices = [{} as RealEstate]
                } catch (error) {
                    console.error(error);
                }
            })()
        } else {
            setToggleIsOn(!toggleIsOn);
        }
    };

    const handleIsOpenClick = () => {
        setToggleIsOpen(!toggleIsOpen);
    };


    const handleIsPrivateClick = () => {
        setToggleIsPrivate(!toggleIsPrivate);
    };


    const renderDeviceSpecificUI = () => {
        switch (props.deviceType) {
            case 'air_conditioning':
                return (
                    <div>
                        <div className={DeviceManagerCSS.row}>
                            <Button text={toggleIsOn ? 'Turn OFF' : 'Turn ON'} onClick={handleIsOnClick} submit={undefined} />
                            <Button text="Custom Mode" onClick={undefined} submit={undefined} />
                        </div>
                        <div className={DeviceManagerCSS.row}>
                            <div>
                                Select Mode:
                                <select className={DeviceManagerCSS.customSelect}>
                                    {props.device.SupportedModes.map((mode, index) => (
                                        <option key={index}>{mode}</option>
                                    ))}
                                </select>
                            </div>
                            <Button text="Set" onClick={undefined} submit={undefined} />
                        </div>
                        <div className={DeviceManagerCSS.row}>
                            <div className={DeviceManagerCSS.row}>
                                Set Temperature:
                                <div className={DeviceManagerCSS.input}>
                                    <InputField usage={'Temperature'} className={''} />
                                </div>
                            </div>
                            <div>
                                <Button text="Set" onClick={undefined} submit={undefined} />
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
