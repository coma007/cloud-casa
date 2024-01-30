import React, { useState } from 'react';
import FilterCSS from './Filter.module.scss'
import Button from '../../../../../components/forms/Button/Button';
import { DeviceService } from '../../../DeviceService';

const FilterDate = (props: { handleSubmit: any, handleFromDateChange: any, fromDate: string, toDate: string, toDateMin: string, setToDate: any, showActivity: boolean }) => {


  return (
    <div>
      <div className={FilterCSS.row}>
        <span className={FilterCSS.left}>
          <label htmlFor="fromDate">Select from date:</label>
          <input className={FilterCSS.input}
            type="date"
            id="fromDate"
            value={props.fromDate}
            onChange={props.handleFromDateChange}
          />
          <label htmlFor="toDate">Select to date:</label>
          <input className={FilterCSS.input}
            type="date"
            id="toDate"
            value={props.toDate}
            onChange={(e) => props.setToDate(e.target.value)}
            min={props.toDateMin}
          />
        </span>
        {!props.showActivity &&
          <Button text={'Filter'} onClick={() => props.handleSubmit(props.fromDate, props.toDate)} submit={undefined} />
        }
      </div>
      <div>
        <small><i>or select a quick filter:</i></small>

        <button className={FilterCSS.smallButton}
          onClick={() => props.handleSubmit((new Date(new Date().getTime() - 60 * 60 * 1000)).toISOString(), (new Date()).toISOString())}>
          last hour
        </button>
        {props.showActivity &&
          <button className={FilterCSS.smallButton}
            onClick={() => props.handleSubmit((new Date(new Date().getTime() - 2 * 60 * 60 * 1000)).toISOString(), (new Date()).toISOString())}>
            last 3 hours
          </button>
        }
        {props.showActivity &&
          <button className={FilterCSS.smallButton}
            onClick={() => props.handleSubmit((new Date(new Date().getTime() - 6 * 60 * 60 * 1000)).toISOString(), (new Date()).toISOString())}>
            last 6 hours
          </button>
        }
        <button className={FilterCSS.smallButton}
          onClick={() => props.handleSubmit((new Date(new Date().getTime() - 12 * 60 * 60 * 1000)).toISOString(), (new Date()).toISOString())}>
          last 12 hours
        </button>
        <button className={FilterCSS.smallButton}
          onClick={() => props.handleSubmit((new Date(new Date().getTime() - 24 * 60 * 60 * 1000)).toISOString(), (new Date()).toISOString())}>
          last day
        </button>
        <button className={FilterCSS.smallButton}
          onClick={() => props.handleSubmit((new Date(new Date().getTime() - 30 * 24 * 60 * 60 * 1000)).toISOString(), (new Date()).toISOString())}>
          last month
        </button>
      </div>
    </div>
  );
};

export default FilterDate;
