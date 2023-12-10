import Tooltip from "../../view/Tooltip/Tooltip";
import ImageButtonCSS from "./ImageButton.module.scss"
import TooltipCSS from "../../view/Tooltip/Tooltip.module.scss"
import { MouseEventHandler } from "react";

const ImageButton = (props: { path: string, tooltipText: string, onClick: MouseEventHandler<HTMLButtonElement> }) => {
    return (
        <button onClick={props.onClick} className={`${ImageButtonCSS.button} ${TooltipCSS.bottomTooltip}`}>
            <img src={props.path}></img>
            <Tooltip tooltipText={props.tooltipText} />
        </button>
    )
}

export default ImageButton;