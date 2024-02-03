import axios from "axios";
import { ESTATE_GET_ALL, ESTATE_GET_ALL_APPROVED, ESTATE_GET_ALL_APPROVED_BY_OWNER, ESTATE_GET_ALL_BY_OWNER, ESTATE_IS_OWNER, ESTATE_REGISTER } from "../../api";
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

    getAll: async function (): Promise<RealEstate[]> {
        let response: ServiceResponse<RealEstate[]> = await ApiService.makeRequest('get', ESTATE_GET_ALL());
        return response.data;
    },

    getAllApproved: async function (): Promise<RealEstate[]> {
        let response: ServiceResponse<RealEstate[]> = await ApiService.makeRequest('get', ESTATE_GET_ALL_APPROVED());
        return response.data;
    },

    isOwner: async function (estateId: number): Promise<any> {
        let response: ServiceResponse<any> = await axios.get(ESTATE_IS_OWNER(estateId));
        return response.data;
    },
}