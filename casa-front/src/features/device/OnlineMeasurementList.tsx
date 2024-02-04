export interface OnlineMeasurementList {
    deviceId: number;
    from: Date;
    to: Date;
    counts: any;
    hourly: boolean,
    maxCount: number,
    totalMaxCount: number,
    delay: number
}