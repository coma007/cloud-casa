import { FormikProps, FormikValues } from 'formik';
import React, { useRef, useState } from 'react'
import InputField from '../../../../../components/forms/InputField/InputField';
import ModalWindow from '../../../../../components/view/Modal/ModalWindow';
import { DeviceService } from '../../../DeviceService';
import CreateScheduleModalCSS from "./CreateScheduleModal.module.scss"
import { Form } from 'react-router-dom';
import TimePicker from 'react-time-picker';

const CreateScheduleModalSprinklerSystem = (props: { show: any, setShow: any, device: any }) => {

    const [startTime, setStartTime] = useState("");
    const [endTime, setEndTime] = useState("");

    const [days, setDays] = useState([false, false, false, false, false, false, false]);



    const handleSubmit = () => {
        let now = new Date();

        const [hours, minutes] = startTime.split(':').map(Number);
        const startDateTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), hours, minutes, 0);

        const [endHours, endMinutes] = endTime.split(':').map(Number);
        const endDateTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), endHours, endMinutes, 0);

        try {
            DeviceService.createSprinklerSchedule(props.device.Id, { startTime: startDateTime, endTime: endDateTime, scheduledDays: days });
        } catch (error) {
            console.error(error);
        }

    };

    const handleFromChange = (e) => {
        let time = e.target['value'];
        setStartTime(time);
    }

    const handleToChange = (e) => {
        let time = e.target['value'];
        setEndTime(time);
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
            height="50%"
            width='46%'
            isOpen={props.show}
            closeWithdrawalModal={closeModal}
            okWithdrawalModal={handleSubmit}
            title="Schedule Sprinkler System"
            formId='NULL VALUE'
            buttonText="Change" >
            <div className={CreateScheduleModalCSS.rowTime}>
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
            <label>Select days</label>
            <div className={CreateScheduleModalCSS.row}>
                {['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday'].map((day, index) => (
                    <label key={index.toString()} style={{ background: days[index] ? '#32f0da' : '#32f0da21' }} className={CreateScheduleModalCSS.dayLabel}>
                        <input
                            type="checkbox"
                            style={{ display: 'none' }} // Hide the checkbox
                            checked={days[index]}
                            onChange={() => handleDayChange(index)}
                        />
                        {day}
                    </label>
                ))}
            </div>
        </ModalWindow>
    )
}


export default CreateScheduleModalSprinklerSystem