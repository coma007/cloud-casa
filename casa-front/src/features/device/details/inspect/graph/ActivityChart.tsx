import React, { useEffect, useState } from 'react';
import { Bar, Pie } from 'react-chartjs-2';
import GraphCSS from './Graph.module.scss';
import { OnlineMeasurementList } from '../../../OnlineMeasurementList';
import { hover } from '@testing-library/user-event/dist/hover';

const ActivityChart = (props: { data: OnlineMeasurementList | undefined }) => {

  let formattedData = {};

  useEffect(() => {
    console.log(props.data);
    if (props.data) {
      formattedData = {};
    for (const key in props.data?.counts) {
      const timestamp = new Date(key)
      const formattedTime = `${timestamp.getDate()}.${timestamp.getMonth() + 1}.${timestamp.getFullYear()}. ${timestamp.getHours()}:${timestamp.getMinutes()}:${timestamp.getSeconds()}`
      formattedData[formattedTime] = props.data.counts[key];
    }
    props.data.counts = formattedData;
    }
  }, [props.data])


  if (!props.data) {
    return <div></div>;
  }

  const { counts, maxCount, totalMaxCount } = props.data;

  const barChartData = {
    labels: Object.keys(counts),
    datasets: [
      {
        label: 'Online time percentage',
        backgroundColor: 'rgba(75,192,192,0.4)',
        borderColor: 'rgba(75,192,192,1)',
        borderWidth: 1,
        hoverBackgroundColor: 'rgba(75,192,192,0.6)',
        hoverBorderColor: 'rgba(75,192,192,1)',
        data: Object.values(counts).map((count: any) => count / props.data!.maxCount * 100),
      },
    ],
  };

  const totalCount = Object.values(counts).reduce((acc: number, count: any) => acc + parseInt(count, 10), 0);
  const pieChartData = {
    labels: ['Online hours', 'Offline hours'],
    datasets: [
      {
        data: [totalCount / 4 / 60, (totalMaxCount - totalCount) / 4 / 60],
        backgroundColor: ['rgba(75,192,192,0.4)', 'rgba(200,75,75,0.5)'],
        hoverBackgroundColor: ['rgba(75,192,192,1)', 'rgba(200,75,75,1.0)'],
      },
    ],
    
  };

  const pieChartOptions = {
    tooltips: {
      enabled: false,
    },
  };


  return (
    <div className={GraphCSS.grid}>
      <div>
        <b>Activity percentage by {props.data.hourly ? "hour" : "day"}</b>
        <Bar data={barChartData} />
      </div>
      <div>
        <b>Activity in hours</b>
        {/* <Pie data={pieChartData} options={ {plugins: {tooltip: {enabled: false}}}}/> */}
        <Pie data={pieChartData}/>
      </div>
    </div>
  );
};

export default ActivityChart;
