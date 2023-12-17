export interface DeviceMeasurementList {
    deviceType : string;
    deviceId : number;
    from : Date;
    to : Date;
    measurements : any[];
}