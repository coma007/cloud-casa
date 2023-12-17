import React, { useState } from 'react'
import { DeviceMeasurementList } from '../../../DeviceMeasurementList'
import Table, { TableRow } from '../../../../../components/tables/Table/Table'

const DetailsTable = (props : { deviceType : string, measurements : DeviceMeasurementList}) => {
    let header ;
    console.log(props.deviceType)
    if (props.deviceType === "solar_panel_system") {
        header = {
            rowData: [
                { content: "Device", widthPercentage: 40},
                { content: "Command", widthPercentage: 40},
                { content: "User", widthPercentage: 40},
            ],
            onClick: undefined
        };
    }
    const [headers, setHeaders] = useState<TableRow>(header)
    return (
        // <div>Table</div>
       <Table headers={headers} rows={[headers]} />
    )
}

export default DetailsTable