import axios from "axios";
import { AIR_CONDITION_MODE, AIR_CONDITION_SCHEDULE, AIR_CONDITION_TEMPERATURE, AIR_CONDITION_WORKING, DEVICE_FILTER, DEVICE_GET_ALL_BY_OWNER, DEVICE_GET_DETAILS, DEVICE_REGISTER } from "../../api";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { DeviceCreate, DeviceDetails, ModeCommand, Schedule, TemperatureCommand, WorkingCommand } from "./Device";
import { DeviceMeasurementList } from "./DeviceMeasurementList";

export const DeviceService = {
    register: async function (formData: DeviceCreate): Promise<DeviceCreate> {
        let response: ServiceResponse<DeviceCreate> = await axios.post(DEVICE_REGISTER(), formData);

        return response.data;
    },

    filter: async function (id: number, measurement: string, from: string, to: string, username: string): Promise<DeviceMeasurementList> {
        let response: ServiceResponse<DeviceMeasurementList> = await axios.get(DEVICE_FILTER(id, measurement, from, to, username));

        return response.data;
    },

    getAllByOwner: async function (): Promise<DeviceDetails[]> {
        let response: ServiceResponse<DeviceDetails[]> = await axios.get(DEVICE_GET_ALL_BY_OWNER());

        return response.data;
    },

    getDeviceDetails: async function (deviceId : number): Promise<any> {
        let response: ServiceResponse<any> = await axios.get(DEVICE_GET_DETAILS(deviceId));

        return response.data;
    },


    sendWorkingCommand: async function (command: WorkingCommand): Promise<any> {
        try {
            let response = await axios.post(AIR_CONDITION_WORKING(), command);
            return response.data;
        } catch (error) {
            console.error(error);
        }
    },

    sendTemperatureCommand: async function (command: TemperatureCommand): Promise<any> {
        try {
            let response = await axios.post(AIR_CONDITION_TEMPERATURE(), command);
            return response.data;
        } catch (error) {
            console.error(error);
        }
    },

    sendModeCommand: async function (command: ModeCommand): Promise<any> {
        try {
            let response = await axios.post(AIR_CONDITION_MODE(), command);
            return response.data;
        } catch (error) {
            console.error(error);
        }
    },

    createSchedule: async function (schedule: Schedule): Promise<any> {
        try {
            let response = await axios.post(AIR_CONDITION_SCHEDULE(), schedule);
            return response.data;
        } catch (error) {
            console.error(error);
        }
    },
}