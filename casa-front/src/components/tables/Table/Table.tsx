import { MouseEventHandler, useState } from "react";
import TableCSS from "./Table.module.scss"
import TableRow, { TableRowData } from "../TableRow/TableRow";

export interface TableRow {
    rowData: TableRowData[],
    onClick: MouseEventHandler<HTMLDivElement> | undefined
}

// TODO make table resizable
const Table = (props: { headers: TableRow, rows: TableRow[] }) => {
    const [columnWidths, setColumnWidths] = useState<number[]>(props.headers.rowData.map(header => header.widthPercentage));

    const rows = props.rows.map((row) =>
        row.rowData.map((cell, columnIndex, onClick) => ({
            content: cell.content,
            widthPercentage: columnWidths[columnIndex],
            onClick: row.onClick,
        }))
    );

    return (
        <div>
            <TableRow className={TableCSS.header} key="headers" data={props.headers.rowData} onClick={props.headers.onClick}/>
            {rows.map((row, index) => (
                <TableRow className="" key={index} data={row} onClick={row[0].onClick}/>
            ))}
        </div>
    )
}

export default Table;