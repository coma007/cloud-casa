import React, { useEffect, useState } from 'react';
import Card from '../../../../components/view/Card/Card';
import DeviceInfoCSS from './DeviceInfo.module.scss';
import SockJS from 'sockjs-client';
import * as Stomp from 'stompjs';
import { WebSocketService } from '../../../../api/websocket/WebSocketService';
import { DeviceService } from '../../DeviceService';

const DeviceInfo = (props: { deviceType: string; device: any }) => {

    const [status, setStatus] = useState(props.device.Status);
    
    const [plateInput, setPlateInput] = useState('');

    useEffect(()=> {
        setStatus(props.device.Status)
    }, [props.device.Status])

    useEffect(()=>{
        if (props.deviceType === "solar_panel_system") {
            WebSocketService.createSocket('/topic/solar-panel-system-status/'+ props.device.Id, handleSolarPanelMessage);
        }
    }, [props.device.Id])
    
    function handleSolarPanelMessage(message: {topic : string, message : string, fromId : string, toId : string, attachment : string}){
        setStatus(message.message)
    }

    const handleRemoveVehicle = (plates) => {
        DeviceService.gateRemoveLicencePlates(props.device.Id, plates);
        props.device.AllowedVehicles = props.device.AllowedVehicles.filter(item => item !== plates);
    };

    const handleAddVehicle = () => {
        DeviceService.gateAddLicencePlates(props.device.Id, plateInput);
        props.device.AllowedVehicles.push(plateInput);
        setPlateInput("");
    };

    const additionalProperties = () => {
        switch (props.deviceType) {
            case 'air_conditioning':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>MIN / MAX TEMPERATURE:</b> {props.device.MinTemperature} / {props.device.MaxTemperature}
                        </p>
                        <p className={DeviceInfoCSS.row}>
                            <b>SUPPORTED MODES:</b> <small><i>use scroll to view all modes bellow</i></small>
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

            case 'washing_machine':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>SUPPORTED MODES:</b> <small><i>use scroll to view all modes bellow</i></small>
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

            case 'electric_vehicle_charger':
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

            case 'house_battery':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>SIZE:</b> <span>{props.device.Size}</span>
                        </p>
                    </>
                );

            case 'solar_panel_system':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
                            <b>SIZE:</b> <span>{props.device.Size} m<sup>2</sup></span>
                        </p>
                        <p className={DeviceInfoCSS.row}>
                            <b>EFFICIENCY:</b> {props.device.Efficiency}
                        </p>
                    </>
                );

            case 'vehicle_gate':
                return (
                    <>
                        <hr />
                        <p className={DeviceInfoCSS.row}>
        <b>ALLOWED VEHICLES:</b> <small><i>use scroll to view all vehicles below</i></small>
        </p>
        <p className={DeviceInfoCSS.inputRow}    >
        <input
            type="text"
            maxLength={8}
            placeholder="Enter vehicle plate"
            value={plateInput}
            onChange={(e) => setPlateInput(e.target.value)}
        />
        <button onClick={handleAddVehicle}>Add</button>
        </p>
    
                    <div className={DeviceInfoCSS.scrollableListContainer}>
                        <ul className={DeviceInfoCSS.scrollableList}>
                            {props.device.AllowedVehicles.map((mode, index) => (
                                <li key={index}>
                                    <span>
                                    {mode}
                                    </span>
                                    <button 
                                    style={{
                                        background: 'none',
                                        border: 'none', 
                                        cursor: 'pointer', 
                                    }}
                                    onClick={() => {handleRemoveVehicle(mode)} }>Remove</button>
                                </li>
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
                <b>STATUS:</b> {status}
            </p>
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
