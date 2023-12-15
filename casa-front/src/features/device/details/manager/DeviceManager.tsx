import React, { useState } from 'react';
import Card from '../../../../components/view/Card/Card';
import Button from '../../../../components/forms/Button/Button';
import DeviceManagerCSS from './DeviceManager.module.scss'

const DeviceManager = (props: { deviceType: string; device: any }) => {
    const [isToggleOn, setToggleOn] = useState(false);

    const handleToggleClick = () => {
        setToggleOn(!isToggleOn);
    };

    const renderDeviceSpecificUI = () => {
        switch (props.deviceType) {
            case 'AirConditioning':
                return (
                    <div>
                        <p>
                            <Button text={isToggleOn ? 'Turn OFF' : 'Turn ON'} onClick={handleToggleClick} submit={undefined} />
                        </p>
                        <p>
                            Choose Regime:
                            <select>
                                {props.device.SupportedModes.map((mode, index) => (
                                    <option key={index}>{mode}</option>
                                ))}
                            </select>
                            <Button text="Set" onClick={undefined} submit={undefined} />
                        </p>
                        <p>
                            Set Temperature:
                            <input type="text" />
                            <Button text="Set" onClick={undefined} submit={undefined} />
                        </p>
                        <p>
                            <button>Create Custom Mode</button>
                        </p>
                    </div>
                );

            case 'WashingMachine':
                return (
                    <div>
                        <p>
                            Select Mode:
                            <select>
                                {props.device.SupportedModes.map((mode, index) => (
                                    <option key={index}>{mode}</option>
                                ))}
                            </select>
                        </p>
                        <p>
                            <Button text={isToggleOn ? 'STOP' : 'START'} onClick={handleToggleClick} submit={undefined} />
                            <Button text="Schedule Washing" onClick={undefined} submit={undefined} />
                        </p>
                    </div>
                );

            case 'SolarPanelSystem':
                return (
                    <div>
                        <Button text={isToggleOn ? 'Turn OFF' : 'Turn ON'} onClick={handleToggleClick} submit={undefined} />
                    </div>
                );

            case 'VehicleGate':
                return (
                    <div className={DeviceManagerCSS.row}>
                        <Button text={isToggleOn ? 'CLOSE' : 'OPEN'} onClick={handleToggleClick} submit={undefined} />
                        <Button text={isToggleOn ? 'Set to PUBLIC' : 'Set to PRIVATE'} onClick={handleToggleClick} submit={undefined} />
                    </div>
                );

            case 'SprinklerSystem':
                return (
                    <div>
                        <p>
                            <Button text={isToggleOn ? 'Turn OFF' : 'Turn ON'} onClick={handleToggleClick} submit={undefined} />
                        </p>
                    </div>
                );

            default:
                return null;
        }
    };

    return (
        <Card>
            <h5><b>Device Manager</b></h5> <br/>
            {renderDeviceSpecificUI()}
        </Card>
    );
};

export default DeviceManager;
