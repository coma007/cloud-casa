import React, { Component } from 'react';
import InputFieldCSS from "./InputField.module.scss"

const InputField = ({ usage, className, value, onChange, disabled, type, refFile }: { usage: string, className: string, value?: string, onChange?: any, disabled?: boolean, type?: string, refFile?: any }) => {
    const inputFieldType = ["text", "file", "number"]
    if (type === undefined) {
        if (usage.toLowerCase().includes("password")) {
            type = "password"
        } else if (!inputFieldType.includes(usage.toLowerCase())) {
            type = "text"
        } else {
            type = usage.toLowerCase()
        }
    }
    return (
        <span>
            {disabled === undefined ?
            (<input className={`${InputFieldCSS.input}, ${className}`} placeholder={usage} value={value} onChange={onChange} ref={refFile} type={type.toLowerCase()} />)
            : (<input disabled className={`${InputFieldCSS.input}, ${className}`} placeholder={usage} value={value} onChange={onChange} ref={refFile} type={type.toLowerCase()} />)
}
        </span>
    )
}

export default InputField