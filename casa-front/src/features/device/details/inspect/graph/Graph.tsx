import React, { useState, useEffect } from 'react';
import { Line } from 'react-chartjs-2';
import { CDBContainer } from 'cdbreact';

import { Chart, registerables } from 'chart.js';
import GraphCSS from './Graph.module.scss';
import { WebSocketService } from '../../../../../api/websocket/WebSocketService';
import { DeviceMeasurementList } from '../../../DeviceMeasurementList';
import { Card } from 'react-bootstrap';

Chart.register(...registerables);


const Graph = (props: { label: string, deviceType: string, measurements : DeviceMeasurementList, ambientMeasurement?: string }) => {
    const [data, setData] = useState<{ labels: any, datasets: any } | undefined>(undefined);
    const [graphData, setGraphData] = useState<({value: number|null, timestamp: string | null})[]>([])
    const [tagName, setTagName] = useState<string>("");
    const [showGraph, setShowGraph] = useState(false)

    useEffect(() => {
        console.log(props.deviceType)
        if (Object.keys(props.measurements).length > 0) {
            let newData: ({ value: number | null, timestamp: string | null })[] = []
            // console.log(props.measurements.measurements)
            if (props.deviceType == "house_battery") {
                for (let record of props.measurements.measurements) {
                    // console.log(record)
                    if (record !== undefined) {
                        const timestamp = new Date(record.timestamp * 1000)
                        const formattedTime = `${timestamp.getDate()}.${timestamp.getMonth() + 1}.${timestamp.getFullYear()}. ${timestamp.getHours()}:${timestamp.getMinutes()}:${timestamp.getSeconds()}`
                        newData = [{ value: record.power, timestamp: formattedTime }, ...newData]
                    }
                }
            }
            else if (props.deviceType == "lamp") {
                for (let record of props.measurements.measurements) {
                    console.log(record)
                    if (record !== undefined) {
                        const timestamp = new Date(record.timestamp * 1000)
                        const formattedTime = `${timestamp.getDate()}.${timestamp.getMonth() + 1}.${timestamp.getFullYear()}. ${timestamp.getHours()}:${timestamp.getMinutes()}:${timestamp.getSeconds()}`
                        newData = [{ value: record.brightness, timestamp: formattedTime }, ...newData]
                    }
                }
            }
            else if (props.deviceType == "ambient_sensor") {
                for (let record of props.measurements.measurements) {
                    console.log(record)
                    if (record !== undefined) {
                        const timestamp = new Date(record.timestamp * 1000)
                        const formattedTime = `${timestamp.getDate()}.${timestamp.getMonth() + 1}.${timestamp.getFullYear()}. ${timestamp.getHours()}:${timestamp.getMinutes()}:${timestamp.getSeconds()}`
                        newData = [{ value: record[props.ambientMeasurement!], timestamp: formattedTime }, ...newData]
                    }
                }
            }
            let length = newData.length;
            while (length < 5) {
                newData = [{ value: null, timestamp: null }, ...newData];
                length = newData.length;
            }
            setGraphData(newData)
            setShowGraph(true)
        }
    }, [props.measurements, props.ambientMeasurement])

    useEffect(() => {
        if (graphData.length === 0) {
            return
        }
        
        let labels : string[] = [];
        let data : (number|null)[] = [];
        for (let value of graphData) {
            if (value.value === null) {
                labels.push("")
            } else {
                labels.push(value.timestamp!)
            }
            data.push(value.value)
        }

        let newDataSet = {
            label: props.label,
            fill: false,
            lineTension: 0.1,
            borderColor: '#32F0D9',
            pointBorderWidth: 1,
            pointHoverRadius: 5,
            data: data,
        };

        let newState = {
            labels: labels,
            datasets: [newDataSet],
        };

        setData(newState);
    }, [graphData]); // change on ws, remove "data"


    return (
        <div>
            {showGraph && (
                <><Card>
                    <CDBContainer>
                        {data !== undefined &&
                            <Line data={data} options={{
                                // responsive: true, scales: {
                                //     y: {
                                //         suggestedMin: props.de.range.min,
                                //         suggestedMax: props.selectedTag.range.max
                                //     }
                                // }
                            }} height={"100px"} />
                        }
                    </CDBContainer>
                </Card> </>)
            }
        </div>
    );
};

export default Graph;