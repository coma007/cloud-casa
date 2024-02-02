const url = "http://localhost:8080/api"

export const LOGIN = () => url + "/login";
export const REGISTER = () => url + "/register";
export const REGISTER_ADMIN = () => url + "/admin";
export const CHANGE_PASSWORD = () => url + "/user/change-password";
export const SUPER_AND_INIT = () => url + "/user/init";
export const GET_ALL_USERS = () => url + "/user/public";

export const ESTATE_REGISTER = () => url + "/realEstate/create";
export const ESTATE_GET_ALL_BY_OWNER = () => url + "/realEstate/getAllByOwner";
export const ESTATE_GET_ALL_APPROVED_BY_OWNER = () => url + "/realEstate/getAllApprovedByOwner";

export const REQUEST_MANAGE = () => url + "/realEstateRequest/manage";
export const REQUEST_GET_ALL = () => url + "/realEstateRequest/getAll";
export const REQUEST_GET_ALL_UNRESOLVED = () => url + "/realEstateRequest/getAllUnresolved";

export const DEVICE_REGISTER = () => url + "/device/register"
export const DEVICE_GET_ALL_BY_OWNER = () => url + "/device/getAllByOwner"
export const DEVICE_GET_ALL_BY_REAL_ESTATE = (id: number) => url + "/device/getAllByRealEstate/" + id
export const DEVICE_TOGGLE_SOLAR_PANEL_SYSTEM = (id: number) => url + "/solarPanelSystem/toggleStatus/" + id
export const DEVICE_GET_DETAILS = (id: number) => url + "/device/getDeviceDetails/" + id

export const CHARGER_START_CHARGING = (id : number, slot : number) => url + "/electricVehicleCharger/startCharging/" + id + "/" + slot 
export const CHARGER_END_CHARGING = (id : number, slot : number) => url + "/electricVehicleCharger/endCharging/" + id + "/" + slot 
export const CHARGER_SET_MAX_PERCENTAGE = (id : number, slot : number, max : number) => url + "/electricVehicleCharger/setMaxPercentage/" + id + "/" + slot + "/" + max

export const AIR_CONDITION_TEMPERATURE = () => url + "/airConditioning/simulation/temperature"
export const AIR_CONDITION_MODE = () => url + "/airConditioning/simulation/mode"
export const AIR_CONDITION_WORKING = () => url + "/airConditioning/simulation/working"
export const AIR_CONDITION_SCHEDULE = () => url + "/airConditioning/simulation/schedule"
export const SPRINKLER_SCHEDULE = (deviceId) => url + "/sprinklerSystem/setSchedule/" + deviceId;

export const POWER_USAGE_FOR_CITY = (from: string, to: string) => url + "/houseBattery/powerUsageForCity?" + `from=${from}&` + `to=${to}`;
export const POWER_USAGE_FOR_ESTATE = (from: string, to: string) => url + "/houseBattery/powerUsage?" + `from=${from}&` + `to=${to}`;

export const DEVICE_FILTER = (id: number, measurement: string, from: string, to: string, username: string, page : number) => url + "/device/filter?" + `id=${id}&` + `measurement=${measurement}&` + `from=${from}&` + `to=${to}&` + `username=${username}&` + `page=${page}`;
export const ACTIVITY_FILTER = (id: number, from: string, to: string) => url + "/device/filterActivity?" + `id=${id}&` + `from=${from}&` + `to=${to}&`;
export const DEVICE_GET_PAGE_NUMBER = (id: number, measurement: string, from: string, to: string, username: string) => url + "/device/filterPages?" + `id=${id}&` + `measurement=${measurement}&` + `from=${from}&` + `to=${to}&` + `username=${username}`;
export const LOCATION_CITIES = (country) => url + "/location/getAllCities/" + country;
export const LOCATION_COUNTRIES = () => url + "/location/getAllCountries";

export const LAMP_MANAGER = (deviceId) => url + "/lamp/turnOn/" + deviceId;
export const GATE_MANAGER = (deviceId, func) => url + "/vehicleGate/" + func + "/" + deviceId;

export const WEBSOCKET = () => "http://localhost:8080/socket";
