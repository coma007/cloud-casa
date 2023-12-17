import React, { useEffect, useState } from 'react'
import { DeviceMeasurementList } from '../../../DeviceMeasurementList'
import Table, { TableRow } from '../../../../../components/tables/Table/Table'

const DetailsTable = (props : { deviceType : string, measurements : DeviceMeasurementList}) => {
    let header, data;
    useEffect(() => {
        if (props.measurements.measurements !== undefined) {
            if (props.deviceType === "solar_panel_system") {
                header = {
                    rowData: [
                        { content: "Time", widthPercentage: 40},
                        { content: "Command", widthPercentage: 40},
                        { content: "User", widthPercentage: 40},
                    ],
                    onClick: undefined
                }
                setHeaders(header);
                data = [] as TableRow[]
                console.log(props.measurements)
                props.measurements.measurements.forEach(m => {
                    const timestamp = new Date(m.timestamp * 1000)
                    const formattedTime = `${timestamp.getDate()}.${timestamp.getMonth() + 1}.${timestamp.getFullYear()}. ${timestamp.getHours()}:${timestamp.getMinutes()}:${timestamp.getSeconds()}`
                    data.push({
                        rowData: [
                            { content: formattedTime, widthPercentage: 40},
                            { content: m.command, widthPercentage: 40},
                            { content: m.user, widthPercentage: 40},
                            
                        ],
                        onClick: undefined
                    })
                });
                setRows(data);
                setShowTable(true)
            }
        }
    }, [props.measurements])
    const [headers, setHeaders] = useState<TableRow>(header)
    const [rows, setRows] = useState<TableRow[]>(data)
    const [showTable, setShowTable] = useState(false)
    return (
        <div>
            {showTable &&
                <Table headers={headers} rows={rows} />
            }
       </div>
    )
}

export default DetailsTable