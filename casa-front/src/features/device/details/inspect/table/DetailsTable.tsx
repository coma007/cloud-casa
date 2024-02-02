import React, { useEffect, useState } from 'react'
import { DeviceMeasurementList } from '../../../DeviceMeasurementList'
import Table, { TableRow } from '../../../../../components/tables/Table/Table'
import DeviceTableCSS from './DetailsTable.module.scss'

const DetailsTable = (props: { deviceType: string, measurements: DeviceMeasurementList, topic?: string }) => {
    let header, data;

    const parseAirConditioningCommand = (measurement) => {
        let result = measurement.executed;
        let command = "";
        if ("working" in measurement) command = measurement.working;
        if ("temperature" in measurement) command = "SET TEMPERATURE TO " + measurement.temperature;
        if ("mode" in measurement) command = "SET MODE TO " + measurement.mode;

        let user = measurement.user;
        const timestamp = new Date(measurement.timestamp * 1000)
        const formattedTime = `${timestamp.getDate()}.${timestamp.getMonth() + 1}.${timestamp.getFullYear()}. ${timestamp.getHours()}:${timestamp.getMinutes()}:${timestamp.getSeconds()}`

        let rowData = [
            { content: formattedTime, widthPercentage: 25 },
            { content: command, widthPercentage: 25 },
            { content: result, widthPercentage: 25 },
            { content: user, widthPercentage: 25 },
        ]
        return rowData;
    }
    useEffect(() => {
        console.log("AAAAAA", props.measurements.measurements)
        if (props.measurements.measurements !== undefined) {
            if (props.deviceType === "solar_panel_system" || props.deviceType === "vehicle_gate" || props.deviceType == "air_conditioning" || props.deviceType == "sprinkler_system" || props.deviceType === "electric_vehicle_charger") {
                let command = "Command";
                let width = [40, 40, 40];

                if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_command") {
                    command = "Status"
                    width = [40, 40, 25]
                } else if (props.deviceType == "electric_vehicle_charger") {
                    console.log(props.measurements)
                    width = [35, 70, 45]
                } else if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_mode") {
                    command = "Mode"
                    width = [53, 53, 33]
                }
                else if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_licence_plates") {
                    width = [53, 53]
                }
                else if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_vehicles") {
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
                    command = "Scanned Licence Plates"
                    header = {
                        rowData: [
                            { content: "Time", widthPercentage: 50 },
                            { content: command, widthPercentage: 50 },
                        ],
                        onClick: undefined
                    }
                }
                if (props.deviceType == "air_conditioning") {
                    header = {
                        rowData: [
                            { content: "Time", widthPercentage: 25 },
                            { content: "Command", widthPercentage: 25 },
                            { content: "Result", widthPercentage: 25 },
                            { content: "User", widthPercentage: 25 },
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
                    else if (props.deviceType == 'sprinkler_system') {
                        if (m.is_on) {
                            content = "ON"
                        }
                        else if (!m.is_on) {
                            content = "OFF"
                        }
                        if (m.is_schedule) {
                            content = "SCHEDULE CHANGE"
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
                    else if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_vehicles") {
                        content = "remove "
                        if (m.adding) {
                            content = "add "
                        }
                        content += m.vehicle
                    }

                    else if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_licence_plates") {
                        content = m.licence_plates

                    }
                    if (props.deviceType == 'air_conditioning') {

                        data.push({
                            rowData: parseAirConditioningCommand(m),
                            onClick: undefined
                        })
                    }
                    else if (props.deviceType == "vehicle_gate" && props.topic == "vehicle_gate_licence_plates") {
                        data.push({
                            rowData: [
                                { content: formattedTime, widthPercentage: 60 },
                                { content: content, widthPercentage: 50 },
                            ],
                            onClick: undefined
                        })
                    }
<<<<<<< casa-front/src/features/device/details/inspect/table/DetailsTable.tsx

                    console.log(width)
                    if(props.deviceType == 'air_conditioning'){
                        
                        data.push({
                            rowData: parseAirConditioningCommand(m),
                            onClick: undefined
                        })
                    }

=======
>>>>>>> casa-front/src/features/device/details/inspect/table/DetailsTable.tsx
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