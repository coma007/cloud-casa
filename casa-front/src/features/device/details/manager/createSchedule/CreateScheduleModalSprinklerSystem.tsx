import { FormikProps, FormikValues } from 'formik';
import React, { useRef, useState } from 'react'
import InputField from '../../../../../components/forms/InputField/InputField';
import ModalWindow from '../../../../../components/view/Modal/ModalWindow';
import { DeviceService } from '../../../DeviceService';
import CreateScheduleModalCSS from "./CreateScheduleModal.module.scss"
import { Form } from 'react-router-dom';

const CreateScheduleModalSprinklerSystem = (props: { show: any, setShow: any, device: any }) => {

    const [startTime, setStartTime] = useState("");
    const [endTime, setEndTime] = useState("");

    const [days, setDays] = useState([false, false, false, false, false, false, false]);



    const handleSubmit = () => {
        try {
            DeviceService.createSprinklerSchedule(props.device.Id, { startTime: startTime, endTime: endTime, days: days });
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

    const handleDayChange = (index) => {
        const updatedDays = [...days];
        updatedDays[index] = !updatedDays[index];
        setDays(updatedDays);
    };

    const closeModal = () => {
        props.setShow(false);

    };

    return (
        <ModalWindow
            height="45%"
            width='40%'
            isOpen={props.show}
            closeWithdrawalModal={closeModal}
            okWithdrawalModal={handleSubmit}
            title="Schedule Sprinkler System"
            formId='NULL VALUE'
            buttonText="Change" >
            <div className={CreateScheduleModalCSS.row}>
                <span>
                    <label htmlFor="scheduleStart">Select start time</label>
                    <input
                        type="time"
                        id="scheduleStart"
                        name="from"
                        value={startTime}
                        onChange={handleFromChange} />
                </span>
                <span>
                    <label htmlFor="scheduleStart">Select end time</label>
                    <input
                        type="time"
                        id="scheduleEnd"
                        name="to"
                        onChange={handleToChange}
                        value={endTime} />
                </span>
            </div>
            <div className={CreateScheduleModalCSS.row}>
                {['Sunday', 'Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'].map((day, index) => (
                    <>
                        <input
                            key={index.toString()}
                            type="checkbox"
                            checked={days[index]}
                            onChange={() => handleDayChange(index)}
                        />
                        <label htmlFor={index.toString()}>{day}</label>
                    </>
                ))}
            </div>
        </ModalWindow>
    )
}


export default CreateScheduleModalSprinklerSystem