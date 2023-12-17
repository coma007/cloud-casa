import React from 'react'
import ButtonCSS from './Button.module.scss'

const Button = (props: { text: string, onClick: any, submit: "button" | "submit" | "reset" | undefined, className?: string }) => {
    return (
        <button type={props.submit} className={ButtonCSS.button + " " + props.className} onClick={props.onClick}>{props.text}</button>
    )
}

export default Button