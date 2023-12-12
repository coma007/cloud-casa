import MenuItemCSS from './MenuItem.module.scss'
import Tooltip from '../../view/Tooltip/Tooltip'
import TooltipCSS from '../../view/Tooltip/Tooltip.module.scss'
import { Link } from "react-router-dom";

interface IMenuItemProps { image: string | undefined, text : string, className: string, tooltipText: string, tooltip: boolean, path: string, onClick?: any }

const MenuItem = ({ image, text, className, tooltipText, tooltip, path, onClick }: IMenuItemProps) => {
    return (
        <Link to={path} onClick={onClick}
            className={`${tooltip ? TooltipCSS.tooltip : TooltipCSS.noTooltip} ${MenuItemCSS.link}`}>
            {image !== undefined ?
            (<img className={`${MenuItemCSS.item} ${className}`} src={image} />) : 
            (<span className={`${MenuItemCSS.item} ${className}`}>{text}</span>)}
            <Tooltip tooltipText={tooltipText} />
        </Link>
    )
}

export default MenuItem