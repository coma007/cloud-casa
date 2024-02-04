import axios from "axios";
import { ESTATE_GET_ALL, ESTATE_GET_ALL_APPROVED, ESTATE_GET_ALL_APPROVED_BY_OWNER, ESTATE_GET_ALL_BY_OWNER, ESTATE_IS_OWNER, ESTATE_REGISTER } from "../../api";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { RealEstate, RealEstateCreate, RealEstateCreateFlat } from "./RealEstate";

export const EstateService = {
    register: async function (formData: RealEstateCreate): Promise<any> {
        // let response: ServiceResponse<RealEstateCreate> = await axios.post(ESTATE_REGISTER(), this.flatten(formData), {
        //     headers: {
        //       "Content-Type": "multipart/form-data",
        //     },
        //   });
          let response = await axios.post(ESTATE_REGISTER(), this.flatten(formData), {
            headers: {
              "Content-Type": "multipart/form-data",
            },
            maxContentLength: 10000000000,
            maxBodyLength: 100000000000
          });
        return response.data;
    },

    flatten(formData: RealEstateCreate): any{
        return {
            Address: formData.address.address,
            CityCountry: formData.city.country,
            CityName: formData.city.name,
            File: formData.file,
            Latitude: formData.address.latitude,
            Longitude: formData.address.longitude,
            Name: formData.name,
            NumberOfFloors: formData.numberOfFloors,
            Size: formData.size,
            Type: formData.type,
        }
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