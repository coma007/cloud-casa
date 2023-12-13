import axios from "axios";
import { ApiService, ServiceResponse } from "../../api/ApiService";
import { SUPER_AND_INIT } from "../../api";


axios.interceptors.request.use(
  config => {
    const token = localStorage.getItem("token")
    if (config.url && !config.url.includes("maps.googleapis.com")) {
      if (token) {
        config.headers['Authorization'] ='Bearer ' + token;
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

}


