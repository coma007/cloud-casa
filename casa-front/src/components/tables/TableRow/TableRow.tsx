import { MouseEventHandler } from "react";
import TableRowCSS from "./TableRow.module.scss"
import Button from "../../forms/Button/Button";

export interface TableRowData {
    content: any;
    widthPercentage: number;

    onClick?: MouseEventHandler<HTMLDivElement> | undefined
}

const TableRow = (props: { data: TableRowData[], className: string, onClick: MouseEventHandler<HTMLDivElement> | undefined}) => {
    return (
        <div className={`${props.className} ${TableRowCSS.row} ${props.onClick !== undefined ? TableRowCSS.pointer : ""}`} onClick={props.onClick !== undefined ? props.onClick : () => {}}>
            {props.data.map((rowData, index) => (
                <span key={index} style={{ width: `${rowData.widthPercentage}%` }}>
                    {rowData.onClick !== undefined ? 
                        <Button text="Handle" onClick={rowData.onClick} submit={undefined}></Button> :
                        rowData.content
                    }
                </span>
            ))}
        </div>
    )
}

export default TableRow