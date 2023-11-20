import { REQUEST_GET_ALL, REQUEST_MANAGE } from "../../api";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { RealEstate } from "../estate/RealEstate";
import { RealEstateRequest } from "./RealEstateRequest";

export const RequestService = {
    getAll: async function (): Promise<RealEstate[]> {
        let response: ServiceResponse<RealEstate[]> = await ApiService.makeRequest('get', REQUEST_GET_ALL());
        return response.data;
    },

    manage: async function (request: RealEstateRequest): Promise<RealEstateRequest> {
        console.log(request)
        let response: ServiceResponse<RealEstateRequest> = await ApiService.makeRequest('patch', REQUEST_MANAGE(), request);
        return response.data;
    },
}