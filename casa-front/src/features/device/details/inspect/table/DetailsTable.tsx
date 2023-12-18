import React, { useEffect, useState } from 'react'
import { DeviceMeasurementList } from '../../../DeviceMeasurementList'
import Table, { TableRow } from '../../../../../components/tables/Table/Table'
import DeviceTableCSS from './DetailsTable.module.scss'

const DetailsTable = (props: { deviceType: string, measurements: DeviceMeasurementList, topic?: string }) => {
    let header, data;
    useEffect(() => {
        if (props.measurements.measurements !== undefined) {
            if (props.deviceType === "solar_panel_system" || props.deviceType === "vehicle_gate") {
                let command = "Command";
                let width = [40, 40, 40]

                if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_command") {
                    command = "Status"
                    width = [50, 35, 25]
                }
                else if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_mode") {
                    command = "Mode"
                    width = [53, 53, 33]
                }
                header = {
                    rowData: [
                        { content: "Time", widthPercentage: width[0] },
                        { content: command, widthPercentage: width[1] },
                        { content: "User", widthPercentage: width[2] },
                    ],
                    onClick: undefined
                }
                if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_licence_plates") {
                    command = "Licence Plates"
                    header = {
                        rowData: [
                            { content: "Time", widthPercentage: 50 },
                            { content: command, widthPercentage: 50 },
                        ],
                        onClick: undefined
                    }
                }
                setHeaders(header);
                data = [] as TableRow[]
                console.log(props.measurements)
                props.measurements.measurements.forEach(m => {
                    const timestamp = new Date(m.timestamp * 1000)
                    const formattedTime = `${timestamp.getDate()}.${timestamp.getMonth() + 1}.${timestamp.getFullYear()}. ${timestamp.getHours()}:${timestamp.getMinutes()}:${timestamp.getSeconds()}`

                    let content = m.command;

                    if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_command") {
                        if (m.is_open) {
                            content = "OPEN"
                        }
                        else {
                            content = "CLOSED"
                        }
                    }
                    else if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_mode") {
                        if (m.is_private) {
                            content = "PRIVATE"
                        }
                        else {
                            content = "PUBLIC"
                        }
                    }
                    if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_licence_plates") {
                        content = m.licence_plates
                        data.push({
                            rowData: [
                                { content: formattedTime, widthPercentage: 60 },
                                { content: content, widthPercentage: 50 },
                            ],
                            onClick: undefined
                        })
                    }

                    else {
                        data.push({
                            rowData: [
                                { content: formattedTime, widthPercentage: width[0] },
                                { content: content, widthPercentage: width[1] },
                                { content: m.user, widthPercentage: width[2] },
                            ],
                            onClick: undefined
                        })
                    }
                });
                setRows(data);
                setShowTable(true)
            }
        }
    }, [props.measurements, props.topic])

    // if (props.deviceType == "vehicle_gate") {

    // }

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