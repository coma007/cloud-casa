import axios from "axios";
import { POWER_USAGE_FOR_CITY, POWER_USAGE_FOR_ESTATE } from "../../../../api";

export const PowerUsageService = {
    getCityData: async function (from : string, to : string) {
        let response = await axios.get(POWER_USAGE_FOR_CITY(from, to));

        return response.data;
    },

    getEstateData: async function (from : string, to : string) {
        let response = await axios.get(POWER_USAGE_FOR_ESTATE(from, to));

        return response.data;
    }
}