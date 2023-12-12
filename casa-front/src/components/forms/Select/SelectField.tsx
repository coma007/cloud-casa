import React, { Component } from 'react';
import SelectFieldCSS from "./SelectField.module.scss"

const SelectField = ({ usage, className, value, onChange, disabled }: { usage: string, className: string, value?: string, onChange?: any, disabled?: boolean }) => {
    return (
        <span>
            {disabled === undefined ?
            (<input className={`${SelectFieldCSS.input}, ${className}`} placeholder={usage} value={value} onChange={onChange} type={usage.toLowerCase().includes("password") ? "password" : "text"} />)
            : (<input disabled className={`${SelectFieldCSS.input}, ${className}`} placeholder={usage} value={value} onChange={onChange} type={usage.toLowerCase().includes("password") ? "password" : "text"} />)
}
        </span>
    )
}

export default SelectField