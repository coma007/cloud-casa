import axios from "axios";
import { DEVICE_FILTER, DEVICE_REGISTER } from "../../api";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { DeviceCreate } from "./DeviceCreate";
import { DeviceMeasurementList } from "./DeviceMeasurementList";

export const DeviceService = {
    register: async function (formData: DeviceCreate): Promise<DeviceCreate> {
        let response: ServiceResponse<DeviceCreate> = await axios.post(DEVICE_REGISTER(), formData);

        return response.data;
    },

    filter: async function (id: number, deviceType: string, from: string, to: string, username: string): Promise<DeviceMeasurementList> {
        let response: ServiceResponse<DeviceMeasurementList> = await axios.get(DEVICE_FILTER(id, deviceType, from, to, username));

        return response.data;
    },
}