import axios from "axios";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { GET_ALL_USERS, SUPER_AND_INIT } from "../../api";
import { User } from "./auth/types/User";


axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem("token")
    if (config.url && !config.url.includes("maps.googleapis.com")) {
      if (token) {
        config.headers['Authorization'] = 'Bearer ' + token;
      }
    }
    return config
  },
  error => {
    Promise.reject(error)
  }
)

export const UserService = {
  isSuperAndInit: async (): Promise<boolean> => {
    let url = SUPER_AND_INIT();
    let response = await axios.get(url);
    return response.data;
  },

  getAllUsers: async (): Promise<User[]> => {
    let response = await axios.get(GET_ALL_USERS());
    return response.data;
  },
}


