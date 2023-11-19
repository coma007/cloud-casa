import { ApiService, ServiceResponse } from "../../../api/ApiService";
import { Address, City } from "../Location";

export const LocationService = {
    getLocation: async function (lat: number, lng: number): Promise<[Address, City]> {
        let response: any = await ApiService.makeRequest('get', "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + lat + "," + lng + "&key=" + process.env.REACT_APP_GOOGLE_MAPS_GEOCODING_API_KEY + "&sensor=true");

        let address_components = response.data.results[0].address_components;
        let country;
        let city;
        let street;
        let number;
        address_components.forEach(component => {
            if (component.types.includes("country")) {
                country = component.long_name
            }
            if (component.types.includes("locality")) {
                city = component.long_name
            }
            if (component.types.includes("route")) {
                street = component.long_name
            }
            if (component.types.includes("street_number")) {
                number = component.long_name
            }

        });
        let fullAddress: Address = {
            street: street + " " + number,
            latitude: lat,
            longitude: lng,

        }
        let fullCity: City = {
            name: city,
            country: country
        }
        return [fullAddress, fullCity];
    },
}