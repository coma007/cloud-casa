import React from 'react';
import Card from '../../../../components/view/Card/Card';
import DeviceInfoCSS from './DeviceInfo.module.scss';

const DeviceInfo = (props: { deviceType: string; device: any }) => {
    const additionalProperties = () => {
        switch (props.deviceType) {
            case 'AirConditioning':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>MIN TEMPERATURE:</b> {props.device.MinTemperature}
                        </p>
                        <p className={DeviceInfoCSS.row}>
                            <b>MAX TEMPERATURE:</b> {props.device.MaxTemperature}
                        </p>
                    </>
                );

            case 'WashingMachine':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>SUPPORTED MODES:</b>
                        </p>
                        <div className={DeviceInfoCSS.scrollableListContainer}>

                            <ul className={DeviceInfoCSS.scrollableList}>
                                {props.device.SupportedModes.map((mode, index) => (
                                    <li key={index}>{mode}</li>
                                ))}
                            </ul>
                        </div>
                    </>
                );

            case 'ElectricVehicleCharger':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>CHARGE POWER:</b> {props.device.ChargePower}
                        </p>
                        <p className={DeviceInfoCSS.row}>
                            <b>NUMBER OF SLOTS:</b> {props.device.NumOfSlots}
                        </p>
                    </>
                );

            case 'HouseBattery':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>SIZE:</b> {props.device.Size}
                        </p>
                    </>
                );

            case 'SolarPanelSystem':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>SIZE:</b> {props.device.Size}
                        </p>
                        <p className={DeviceInfoCSS.row}>
                            <b>EFFICIENCY:</b> {props.device.Efficiency}
                        </p>
                    </>
                );

            case 'VehicleGate':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>ALLOWED VEHICLES:</b>
                        </p>
                        <div className={DeviceInfoCSS.scrollableListContainer}>
                            <ul className={DeviceInfoCSS.scrollableList}>
                                {props.device.AllowedVehicles.map((mode, index) => (
                                    <li key={index}>{mode}</li>
                                ))}
                            </ul>
                        </div>
                    </>
                );

            default:
                return null;
        }
    };



    return (
        <Card>
            <p className={DeviceInfoCSS.row}>
                <b>NAME:</b> {props.device.Name}
            </p>
            <p className={DeviceInfoCSS.row}>
                <b>ESTATE:</b> {props.device.RealEstateName}
            </p>
            <p className={DeviceInfoCSS.row}>
                <b>POWER SUPPLY:</b> {props.device.PowerSupplyType}
            </p>
            <p className={DeviceInfoCSS.row}>
                <b>ENERGY CONSUMPTION:</b> {props.device.EnergyConsumption}
            </p>
            {additionalProperties()}
        </Card>
    );
};

export default DeviceInfo;
