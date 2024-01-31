import React from 'react'
import ButtonCSS from './Button.module.scss'

const Button = (props: { text: string, onClick: any, submit: "button" | "submit" | "reset" | undefined, className?: string, disabled?: boolean }) => {
    let disabled = false;
    if (props.disabled) {
        disabled = true;
    }
    return (
        <button disabled={props.disabled} type={props.submit} className={(props.disabled ? ButtonCSS.disabled : ButtonCSS.button) + " " + props.className} onClick={props.onClick}>{props.text}</button>
    )
}

export default Button