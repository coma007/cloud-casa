import { REQUEST_GET_ALL } from "../../api";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { RealEstateRequest } from "./RealEstate";

export const RequestService = {
    getAllByOwner: async function (): Promise<RealEstateRequest[]> {
        let response: ServiceResponse<RealEstateRequest[]> = await ApiService.makeRequest('get', REQUEST_GET_ALL());
        return response.data;
    },
}