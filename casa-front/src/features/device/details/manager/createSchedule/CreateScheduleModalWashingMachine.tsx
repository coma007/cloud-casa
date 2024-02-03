import React, { useRef } from 'react'
import CreateScheduleModalCSS from "./CreateScheduleModal.module.scss"
import * as yup from 'yup'
import { Formik, Form, Field, ErrorMessage, FormikProps, FormikValues } from 'formik';
import { useState } from 'react'
import Modal from 'react-bootstrap/Modal';
import InputField from '../../../../../components/forms/InputField/InputField';
import ErrorMsg from '../../../../../components/error/ErrorMsg';
import Button from '../../../../../components/forms/Button/Button';
import ModalWindow from '../../../../../components/view/Modal/ModalWindow';
import { DeviceService } from '../../../DeviceService';
import FilterDate from '../../inspect/filter/FilterDate';

const CreateScheduleModalWashingMachine = (props: { show: any, setShow: any, device: any }) => {
    let initStart = new Date();
    initStart.setDate(initStart.getDate() + 1);
    const [startTime, setStartTime] = useState(initStart.toISOString().substring(0, 16));

    function addMinutes(date, minutes) {
        return new Date(date.getTime() + minutes * 60000);
    }
    let initEnd = addMinutes(initStart, 60);
    const [endTime, setEndTime] = useState(initEnd.toISOString().substring(0, 16));

    const [toggleIsOn, setToggleIsOn] = useState(false);
    const [mode, setMode] = useState<string | undefined>(undefined);

    const formRef = useRef<FormikProps<FormikValues>>(null);

    const handleIsOnClick = () => {
        setToggleIsOn(!toggleIsOn);
    };

    const handleIsOpenClick = () => {
        setToggleIsOn(!toggleIsOn);

    };

    const handleModeChange = (e) => {
        let m = e.target.value;
        if (m === undefined || m === "None")
            m = null;
        setMode(m);
    };

    const handleSubmit = () => {
        try {
            DeviceService.createWashingMachineSchedule({
                startTime: startTime, endTime: endTime, mode: mode,
                 working: !toggleIsOn, deviceId: props.device.Id
            });
        } catch (error) {
            console.error(error);
        }

    };

    const handleFromChange = (e) => {
        let time = e.target['value'];
        setStartTime(time);
        console.log(time);
    }

    const handleToChange = (e) => {
        let time = e.target['value'];
        setEndTime(time);
        console.log(time);
    }

    const closeModal = () => {
        props.setShow(false);

    };

    let min = new Date().toISOString();
    let max = "2026-06-14T00:00";

    return (
        <ModalWindow
            height="65%"
            width='40%'
            isOpen={props.show}
            closeWithdrawalModal={closeModal}
            okWithdrawalModal={handleSubmit}
            title="Create custom mode"
            formId='NULL VALUE'
            buttonText="Change" >
            <div>
                <input
                    type="datetime-local"
                    id="scheduleStart"
                    name="from"
                    value={startTime}
                    onChange={handleFromChange}
                    min={min}
                    max={max} />

                <input
                    type="datetime-local"
                    id="scheduleEnd"
                    name="to"
                    onChange={handleToChange}
                    value={endTime}
                    min={min}
                    max={max} />
                <div className={CreateScheduleModalCSS.row}>
                    <Button text={toggleIsOn ? 'Turn OFF' : 'Turn ON'} onClick={handleIsOnClick} submit={undefined} className={CreateScheduleModalCSS.accentButton} />
                </div>
                <div className={CreateScheduleModalCSS.row}>
                    <div>
                        Select Mode:
                        <select className={CreateScheduleModalCSS.customSelect} onChange={handleModeChange} value={mode} disabled={toggleIsOn}>
                            <option value={undefined}>None</option>
                            {props.device.SupportedModes.map((modeS, index) => (
                                <option key={index} value={modeS}>{modeS}</option>
                            ))}
                        </select>
                    </div>
                </div>
            </div>
        </ModalWindow>
    )
}

export default CreateScheduleModalWashingMachine