import React, { useState } from 'react';
import FilterCSS from './Filter.module.scss'
import Button from '../../../../../components/forms/Button/Button';
import { DeviceService } from '../../../DeviceService';

const FilterDate = (props: { deviceType: string; device: any }) => {
  const [fromDate, setFromDate] = useState('');
  const [toDate, setToDate] = useState('');
  const [toDateMin, setToDateMin] = useState('');

  const handleFromDateChange = (e) => {
    const selectedFromDate = e.target.value;
    setFromDate(selectedFromDate);

    const nextDay = new Date(selectedFromDate);
    nextDay.setDate(nextDay.getDate() + 1);
    setToDateMin(nextDay.toISOString().split('T')[0]);

    setToDate((prevToDate) => {
      if (!prevToDate || selectedFromDate > prevToDate) {
        return selectedFromDate;
      }
      return prevToDate;
    });
  };

  const handleFilterClick = () => {
    DeviceService.filter(props.device.id, props.deviceType, fromDate, toDate, "");
  };

  const handleLastHourClick = () => {
    DeviceService.filter(props.device.id, props.deviceType, (new Date(new Date().getTime() - 60 * 60 * 1000)).toISOString(), (new Date()).toISOString(), "");
  };

  const handleLast12HoursClick = () => {
    DeviceService.filter(props.device.id, props.deviceType, (new Date(new Date().getTime() - 12 * 60 * 1000)).toISOString(), (new Date()).toISOString(), "");
  };

  const handleLastDayClick = () => {
    DeviceService.filter(props.device.id, props.deviceType, (new Date(new Date().getTime() - 24 * 60 * 1000)).toISOString(), (new Date()).toISOString(), "");
  };

  const handleLastMonthClick = () => {
    DeviceService.filter(props.device.id, props.deviceType, (new Date(new Date().getTime() - 30 * 24 * 60 * 1000)).toISOString(), (new Date()).toISOString(), "");
  };

  return (
    <div>
      <div className={FilterCSS.row}>
        <span className={FilterCSS.left}>
          <label htmlFor="fromDate">Select from date:</label>
          <input className={FilterCSS.input}
            type="date"
            id="fromDate"
            value={fromDate}
            onChange={handleFromDateChange}
          />
          <label htmlFor="toDate">Select to date:</label>
          <input className={FilterCSS.input}
            type="date"
            id="toDate"
            value={toDate}
            onChange={(e) => setToDate(e.target.value)}
            min={toDateMin}
          />
        </span>
        <Button text={'Filter'} onClick={handleFilterClick} submit={undefined} />
      </div>
      <div>
        <small><i>or select a quick filter:</i></small>
        <button className={FilterCSS.smallButton} onClick={handleLastHourClick}>last hour</button>
        <button className={FilterCSS.smallButton} onClick={handleLast12HoursClick}>last 12 hours</button>
        <button className={FilterCSS.smallButton} onClick={handleLastDayClick}>last day</button>
        <button className={FilterCSS.smallButton} onClick={handleLastMonthClick}>last month</button>
      </div>
    </div>
  );
};

export default FilterDate;
