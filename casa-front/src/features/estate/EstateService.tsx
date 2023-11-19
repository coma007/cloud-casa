import { ESTATE_REGISTER } from "../../api";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { RealEstateCreate } from "./RealEstate";

export const EstateService = {
    register: async function (formData: RealEstateCreate): Promise<RealEstateCreate> {
        let response: ServiceResponse<RealEstateCreate> = await ApiService.makeRequest('post', ESTATE_REGISTER(), formData);

        return response.data;
    },
}