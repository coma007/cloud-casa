import axios from "axios";
import { DEVICE_FILTER, DEVICE_GET_ALL_BY_OWNER, DEVICE_GET_ALL_BY_REAL_ESTATE, DEVICE_GET_DETAILS, DEVICE_GET_PAGE_NUMBER, DEVICE_REGISTER, DEVICE_TOGGLE_SOLAR_PANEL_SYSTEM, GATE_MANAGER, LAMP_MANAGER } from "../../api";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { DeviceCreate, DeviceDetails } from "./Device";
import { DeviceMeasurementList } from "./DeviceMeasurementList";

export const DeviceService = {
    register: async function (formData: DeviceCreate): Promise<DeviceCreate> {
        let response: ServiceResponse<DeviceCreate> = await axios.post(DEVICE_REGISTER(), formData);

        return response.data;
    },

    filter: async function (id: number, measurement: string, from: string, to: string, username: string, page : number): Promise<DeviceMeasurementList> {
        let response: ServiceResponse<DeviceMeasurementList> = await axios.get(DEVICE_FILTER(id, measurement, from, to, username, page));

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

    getDeviceDetails: async function (deviceId : number): Promise<any> {
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

    toggleSolarPanelSystem: async function (deviceId : number): Promise<any> {
        let response: ServiceResponse<any> = await axios.post(DEVICE_TOGGLE_SOLAR_PANEL_SYSTEM(deviceId));
        return response.data;
    },
}