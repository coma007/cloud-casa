import axios from "axios";
import { ESTATE_GET_ALL_APPROVED_BY_OWNER, ESTATE_GET_ALL_BY_OWNER, ESTATE_REGISTER } from "../../api";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { RealEstate, RealEstateCreate } from "./RealEstate";

export const EstateService = {
    register: async function (formData: RealEstateCreate): Promise<RealEstateCreate> {
        let response: ServiceResponse<RealEstateCreate> = await axios.post(ESTATE_REGISTER(), formData);

        return response.data;
    },

    getAllByOwner: async function (): Promise<RealEstate[]> {
        let response: ServiceResponse<RealEstate[]> = await ApiService.makeRequest('get', ESTATE_GET_ALL_BY_OWNER());
        return response.data;
    },

    getAllApprovedByOwner: async function (): Promise<RealEstate[]> {
        let response: ServiceResponse<RealEstate[]> = await ApiService.makeRequest('get', ESTATE_GET_ALL_APPROVED_BY_OWNER());
        return response.data;
    },
}