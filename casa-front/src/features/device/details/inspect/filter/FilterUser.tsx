import React, { useState } from 'react';
import FilterCSS from './Filter.module.scss'
import InputField from '../../../../../components/forms/InputField/InputField'
import Button from '../../../../../components/forms/Button/Button';


const FilterUser = (props: { username: string, onInputChange: any, handleSubmit: any }) => {

    return (
        <div className={FilterCSS.row}>
            <span className={FilterCSS.left}>
                <label htmlFor="fromDate">Insert username:</label>
                <InputField usage={'Username'} value={props.username} className={FilterCSS.input} onChange={(e: React.ChangeEvent<HTMLInputElement>) => {
                    props.onInputChange(e.target.value);
                }} />
            </span>
            <span>
                <Button text={'Filter'} onClick={props.handleSubmit} submit={undefined} />
            </span>
        </div>
    )
}

export default FilterUser