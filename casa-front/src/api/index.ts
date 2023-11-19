const url = "http://localhost:8080/api"

export const LOGIN = () => url + "/login";
export const REGISTER = () => url + "/register";

export const ESTATE_REGISTER = () => url + "/realEstate/create";
export const ESTATE_GET_ALL_BY_OWNER = () => url + "/realEstate/getAllByOwner";

export const REQUEST_MANAGE = () => url + "/realEstateRequest/manage";
export const REQUEST_GET_ALL = () => url + "/realEstateRequest/getAll";
