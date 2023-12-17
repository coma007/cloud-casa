const url = "http://localhost:8080/api"

export const LOGIN = () => url + "/login";
export const REGISTER = () => url + "/register";
export const REGISTER_ADMIN = () => url + "/admin";
export const CHANGE_PASSWORD = () => url + "/user/change-password";
export const SUPER_AND_INIT = () => url + "/user/init";

export const ESTATE_REGISTER = () => url + "/realEstate/create";
export const ESTATE_GET_ALL_BY_OWNER = () => url + "/realEstate/getAllByOwner";

export const REQUEST_MANAGE = () => url + "/realEstateRequest/manage";
export const REQUEST_GET_ALL = () => url + "/realEstateRequest/getAll";

export const DEVICE_REGISTER = () => url + "/device/register"
export const DEVICE_GET_ALL_BY_OWNER = () => url + "/device/getAllByOwner"
export const DEVICE_GET_DETAILS = (id : number) => url + "/device/getDeviceDetails/" + id
export const DEVICE_FILTER = (id: number, measurement: string, from: string, to: string, username: string) => url + "/device/filter?" + `id=${id}&` + `measurement=${measurement}&` + `from=${from}&` + `to=${to}&` + `username=${username}`;

export const LOCATION_CITIES = (country) => url + "/location/getAllCities/" + country;
export const LOCATION_COUNTRIES = () => url + "/location/getAllCountries";



export const WEBSOCKET = () => "ws://localhost:8080/socket";
