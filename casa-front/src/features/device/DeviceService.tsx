import axios from "axios";
import { ACTIVITY_FILTER, AIR_CONDITION_MODE, AIR_CONDITION_SCHEDULE, AIR_CONDITION_TEMPERATURE, AIR_CONDITION_WORKING, CHARGER_END_CHARGING, CHARGER_SET_MAX_PERCENTAGE as CHARGER_SET_MAX_PERCENTAGE, CHARGER_START_CHARGING, DEVICE_FILTER, DEVICE_GET_ALL_BY_OWNER, DEVICE_GET_ALL_BY_REAL_ESTATE, DEVICE_GET_DETAILS, DEVICE_GET_PAGE_NUMBER, DEVICE_REGISTER, DEVICE_TOGGLE_SOLAR_PANEL_SYSTEM, GATE_MANAGER, LAMP_MANAGER, SPRINKLER_SCHEDULE } from "../../api";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { DeviceCreate, DeviceDetails, ModeCommand, AirConditionerSchedule, TemperatureCommand, WorkingCommand, SprinklerSchedule } from "./Device";
import { DeviceMeasurementList } from "./DeviceMeasurementList";
import { OnlineMeasurementList } from "./OnlineMeasurementList";

export const DeviceService = {
    register: async function (formData: DeviceCreate): Promise<DeviceCreate> {
        let response: ServiceResponse<DeviceCreate> = await axios.post(DEVICE_REGISTER(), formData);

        return response.data;
    },

    filter: async function (id: number, measurement: string, from: string, to: string, username: string, page: number): Promise<DeviceMeasurementList> {
        let response: ServiceResponse<DeviceMeasurementList> = await axios.get(DEVICE_FILTER(id, measurement, from, to, username, page));

        return response.data;
    },

    filterActivity: async function (id: number, from: string, to: string): Promise<OnlineMeasurementList> {
        let response: ServiceResponse<OnlineMeasurementList> = await axios.get(ACTIVITY_FILTER(id, from, to));

        return response.data;
    },


    getPageNumber: async function (id: number, measurement: string, from: string, to: string, username: string): Promise<number> {
        let response: ServiceResponse<number> = await axios.get(DEVICE_GET_PAGE_NUMBER(id, measurement, from, to, username));

        return response.data;
    },

    getAllByOwner: async function (): Promise<DeviceDetails[]> {
        let response: ServiceResponse<DeviceDetails[]> = await axios.get(DEVICE_GET_ALL_BY_OWNER());

        return response.data;
    },

    getAllByRealEstate: async function (id: number): Promise<DeviceDetails[]> {
        let response: ServiceResponse<DeviceDetails[]> = await axios.get(DEVICE_GET_ALL_BY_REAL_ESTATE(id));

        return response.data;
    },

    getDeviceDetails: async function (deviceId: number): Promise<any> {
        let response: ServiceResponse<any> = await axios.get(DEVICE_GET_DETAILS(deviceId));
        return response.data;
    },


    lampManager: async function (deviceId: number): Promise<any> {
        let response: ServiceResponse<any> = await axios.get(LAMP_MANAGER(deviceId));
        return response.data;
    },


    gateManager: async function (deviceId: number, func: string): Promise<any> {
        let response: ServiceResponse<any> = await axios.get(GATE_MANAGER(deviceId, func));
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

    createAirConditionerSchedule: async function (schedule: AirConditionerSchedule): Promise<any> {
        try {
            let response = await axios.post(AIR_CONDITION_SCHEDULE(), schedule);
            return response.data;
        } catch (error) {
            console.error(error);
        }
    },

    createSprinklerSchedule: async function (deviceId: number, schedule: SprinklerSchedule): Promise<any> {
        try {
            let response = await axios.post(SPRINKLER_SCHEDULE(deviceId), schedule);
            return response.data;
        } catch (error) {
            console.error(error);
        }
    },
    toggleSolarPanelSystem: async function (deviceId: number): Promise<any> {
        let response: ServiceResponse<any> = await axios.post(DEVICE_TOGGLE_SOLAR_PANEL_SYSTEM(deviceId));
        return response.data;
    },

    startCharging: async function (deviceId: number, slot: number): Promise<any> {
        let response: ServiceResponse<any> = await axios.get(CHARGER_START_CHARGING(deviceId, slot));
        return response.data;
    },

    endCharging: async function (deviceId: number, slot: number): Promise<any> {
        let response: ServiceResponse<any> = await axios.get(CHARGER_END_CHARGING(deviceId, slot));
        return response.data;
    },

    setMaxPercentage: async function (deviceId: number, slot: number, maxPercentage: number): Promise<any> {
        let response: ServiceResponse<any> = await axios.get(CHARGER_SET_MAX_PERCENTAGE(deviceId, slot, maxPercentage));
        return response.data;
    },
}