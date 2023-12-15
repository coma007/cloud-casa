import React, { useState } from 'react';
import FilterCSS from './Filter.module.scss'
import InputField from '../../../../../components/forms/InputField/InputField'
import Button from '../../../../../components/forms/Button/Button';


const FilterUser = () => {

    const handleFilterClick = () => {

    }
    return (
        <div className={FilterCSS.row}>
            <span className={FilterCSS.left}>
                <label htmlFor="fromDate">Insert username:</label>
                <InputField usage={'Username'} className={FilterCSS.input} />
            </span>
            <span>
                <Button text={'Filter'} onClick={handleFilterClick} submit={undefined} />
            </span>
        </div>
    )
}

export default FilterUser